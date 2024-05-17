package com.echomap.cherryblossomclean.config;

import com.echomap.cherryblossomclean.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import java.security.cert.CertPathBuilder;

import static org.springframework.security.config.Customizer.*;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/maps/**").permitAll())
        .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/report/**").permitAll())
        .authorizeHttpRequests(
            authorize -> authorize.requestMatchers("/api/members/**").permitAll())
            .authorizeHttpRequests(authorize -> authorize.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll())
        .authorizeHttpRequests(
            authorize -> authorize.requestMatchers("/api/report/admin").hasRole("ADMIN"))
        .authorizeHttpRequests(
            authorize -> authorize.requestMatchers("/api/members/admin").hasRole("ADMIN"))
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

    http.addFilterAfter(jwtAuthFilter, CorsFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
