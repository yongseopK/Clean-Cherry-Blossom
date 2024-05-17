package com.echomap.cherryblossomclean.auth;

import com.echomap.cherryblossomclean.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenProvider {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  /**
   * Json Web Token을 생성하는 메서드
   *
   * @param memberEntity 토큰의 내용(클레임)에 포함될 유저정보
   * @return 생성된 json을 암호화한 토큰값
   */
  public String createToken(Member memberEntity) {

    Date expiry = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

    Map<String, Object> claims = new HashMap<>();
    claims.put("email", memberEntity.getEmail());
    claims.put("role", memberEntity.getRole());

    return Jwts.builder()
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
        .setClaims(claims)
        .setIssuer("운영자")
        .setIssuedAt(new Date())
        .setExpiration(expiry)
        .setSubject(memberEntity.getEmail())
        .compact();
  }

  public String createToken(Member memberEntity, boolean autoLogin) {

    Date expiry;
    if(autoLogin) {
      expiry = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));
    } else {
      expiry = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put("email", memberEntity.getEmail());
    claims.put("role", memberEntity.getRole());

    return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
            .setClaims(claims)
            .setIssuer("운영자")
            .setIssuedAt(new Date())
            .setExpiration(expiry)
            .setSubject(memberEntity.getEmail())
            .compact();
  }

  public TokenUserInfo validateAndGetTokenUserInfo(String token) {
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();

    String email = claims.get("email", String.class);
    String roleString = claims.get("role", String.class);

    if (email == null || roleString == null) {
      return null; // 유효하지 않은 토큰인 경우 null 반환
    }

    Member.Role role = Member.Role.valueOf(roleString);
    return TokenUserInfo.builder()
            .email(email)
            .role(role)
            .build();
  }
}
