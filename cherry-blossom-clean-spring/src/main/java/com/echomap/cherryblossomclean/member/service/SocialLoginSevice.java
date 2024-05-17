package com.echomap.cherryblossomclean.member.service;

import com.echomap.cherryblossomclean.auth.TokenProvider;
import com.echomap.cherryblossomclean.exception.DuplicateOAuthEmailException;
import com.echomap.cherryblossomclean.member.dto.request.MemberSignUpRequsetDTO;
import com.echomap.cherryblossomclean.member.dto.response.MemberSignInResponseDTO;
import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static com.echomap.cherryblossomclean.member.entity.Member.PlatformType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginSevice {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    public MemberSignInResponseDTO naverLoginHandler(String credential) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(credential);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();
        //log.info("response : {}", response);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String encodedName = jsonNode.get("response").get("name").asText();
            String email = jsonNode.get("response").get("email").asText();
            String decodedName = StringEscapeUtils.unescapeJava(encodedName);
            String userId = jsonNode.get("response").get("id").asText();

            Optional<Member> existingMember = memberRepository.findByEmail(email);

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                if (member.getPlatformType() == Member.PlatformType.LOCAL) {
                    throw new DuplicateOAuthEmailException("이미 로컬 계정으로 가입된 이메일입니다.");
                }

                String token = tokenProvider.createToken(member);
                return new MemberSignInResponseDTO(member, token);
            } else {
                MemberSignUpRequsetDTO dto =
                        MemberSignUpRequsetDTO.builder()
                                .email(email)
                                .userName(decodedName)
                                .password(userId)
                                .build();

                Member savedMember =
                        memberRepository.save(dto.toEntity(passwordEncoder, NAVER));
                log.warn("platform type : {}", savedMember.getPlatformType());
                String token = tokenProvider.createToken(savedMember);
                return new MemberSignInResponseDTO(savedMember, token);
            }
        } catch (DuplicateOAuthEmailException e) {
            throw new DuplicateOAuthEmailException(e.getMessage());
        } catch (Exception e) {
            log.warn("error : {}", e.getMessage());
            throw new RuntimeException("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    public MemberSignInResponseDTO googleLoginHandler(String credential) {
        try {
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<Member> existingMember = memberRepository.findByEmail(email);
                if (existingMember.isPresent()) {
                    Member member = existingMember.get();
                    if (member.getPlatformType() == LOCAL) {
                        throw new DuplicateOAuthEmailException("이미 로컬 계정으로 가입된 이메일입니다.");
                    }

                    String token = tokenProvider.createToken(member);
                    return new MemberSignInResponseDTO(member, token);
                } else {
                    MemberSignUpRequsetDTO dto =
                            MemberSignUpRequsetDTO.builder()
                                    .email(email)
                                    .userName(name.replaceAll("\\s", ""))
                                    .password(userId)
                                    .build();

                    Member savedMember =
                            memberRepository.save(dto.toEntity(passwordEncoder, GOOGLE));
                    String token = tokenProvider.createToken(savedMember);
                    return new MemberSignInResponseDTO(savedMember, token);
                }
            } else {
                throw new RuntimeException("Invalid ID token.");
            }
        } catch (DuplicateOAuthEmailException e) {
            throw new DuplicateOAuthEmailException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify ID token.", e);
        }
    }
}
