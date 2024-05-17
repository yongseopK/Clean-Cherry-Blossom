package com.echomap.cherryblossomclean.filter;

import com.echomap.cherryblossomclean.auth.TokenProvider;
import com.echomap.cherryblossomclean.auth.TokenUserInfo;
import com.echomap.cherryblossomclean.exception.TokenExpiredException;
import com.echomap.cherryblossomclean.exception.TokenInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain)
          throws ServletException, IOException {

    try {
      String token = parseBearerToken(request);
      //log.info("token : {}", token);

      if (token != null) {
        TokenUserInfo userInfo = tokenProvider.validateAndGetTokenUserInfo(token);

        if (userInfo != null) {
          List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
          authorityList.add(new SimpleGrantedAuthority(userInfo.getRole().toString()));

          AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                  userInfo,
                  null,
                  authorityList
          );

          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
          log.warn("토큰이 유효하지 않습니다.");
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
      }
    } catch (TokenExpiredException ex) {
      log.warn("토큰이 만료되었습니다.", ex);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
    } catch (TokenInvalidException ex) {
      log.warn("토큰이 유효하지 않습니다.", ex);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    } catch (Exception ex) {
      log.error("예기치 않은 예외가 발생했습니다.", ex);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
