package com.echomap.cherryblossomclean.member.service;

import com.echomap.cherryblossomclean.auth.TokenProvider;
import com.echomap.cherryblossomclean.auth.TokenUserInfo;
import com.echomap.cherryblossomclean.exception.ForcedWithdrawalException;
import com.echomap.cherryblossomclean.exception.ValidateEmailException;
import com.echomap.cherryblossomclean.member.dto.request.MemberSignInRequestDTO;
import com.echomap.cherryblossomclean.member.dto.response.MemberInfoResponseDTO;
import com.echomap.cherryblossomclean.member.dto.response.MemberSignInResponseDTO;
import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.echomap.cherryblossomclean.member.entity.Member.PlatformType.LOCAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\."
                    + "[a-zA-Z0-9_+&*-]+)*@"
                    + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";

    public MemberSignInResponseDTO authenticate(MemberSignInRequestDTO dto) {
        if (!dto.getEmail().matches(EMAIL_REGEX)) {
            throw new ValidateEmailException("올바른 형식의 이메일 주소가 아닙니다.");
        }

        Member member =
                memberRepository
                        .findByEmail(dto.getEmail())
                        .orElseThrow(() -> new RuntimeException("가입된 회원이 아닙니다."));

        String inputPassword = dto.getPassword();
        String encodedPassword = member.getPassword();

        if(member.getPlatformType() != LOCAL) {
            throw new RuntimeException("소셜 로그인을 통해 가입한 회원입니다.");
        }

        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        if(member.isStatus()) {
            LocalDateTime expireDate = member.getUpdatedAt();
            LocalDateTime currentDate = LocalDateTime.now();
            if(expireDate.plusDays(7).isAfter(currentDate)) {
                throw new ForcedWithdrawalException(
                        "강제탈퇴 유예기간인 계정입니다. 기간내에 yongseop01@gmail.com 주소로 문의주세요.");
            } else {
                throw new ForcedWithdrawalException("관리자에 의해 강제탈퇴된 계정입니다. 자세한 사유는 yongseop01@gmail.com 주소로 문의주세요.");
            }
        }

        if (member.isWithdrawalRequested()) {
            LocalDateTime withdrawalDate = member.getWithdrawalDate();
            LocalDateTime currentDate = LocalDateTime.now();
            if (withdrawalDate.plusDays(7).isAfter(currentDate)) {
                // 회원 탈퇴 요청 상태이고 7일이 지나지 않은 경우
                throw new RuntimeException("회원 탈퇴 요청 상태입니다. 7일 이내에 로그인하시면 탈퇴 취소가 가능합니다.");
            } else {
                // 회원 탈퇴 요청 상태이고 7일이 지난 경우
                throw new RuntimeException("회원 탈퇴 처리된 계정입니다.");
            }
        }

        String token = tokenProvider.createToken(member, dto.isAutoLogin());

        return new MemberSignInResponseDTO(member, token);
    }

    @Transactional
    public MemberInfoResponseDTO findUserInfo(TokenUserInfo userInfo) {
        Member member =
                memberRepository
                        .findByEmail(userInfo.getEmail())
                        .orElseThrow(() -> new RuntimeException("정보와 일치한 회원이 없습니다."));

        return new MemberInfoResponseDTO(member);
    }

    public boolean validateToken(TokenUserInfo token) {
        try {
            Member member = memberRepository.findByEmail(token.getEmail()).orElseThrow(() -> new RuntimeException("일치한 회원이 없습니다."));
            return member != null;
        } catch (Exception e) {
            return false;
        }
    }
}
