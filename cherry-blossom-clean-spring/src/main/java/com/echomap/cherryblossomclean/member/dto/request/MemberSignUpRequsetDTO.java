package com.echomap.cherryblossomclean.member.dto.request;

import com.echomap.cherryblossomclean.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 요청 DTO")
public class MemberSignUpRequsetDTO {


  @NotBlank
  @Email
  @Schema(description = "이메일", example = "example@email.com")
  private String email;

  @NotBlank
  @Size(min = 8, max = 20)
  @Schema(description = "비밀번호", example = "test1234!")
  private String password;

  @NotBlank
  @Size(min = 2, max = 5)
  @Schema(description = "사용자 이름", example = "홍길동")
  private String userName;

  public Member toEntity(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(this.email)
            .password(passwordEncoder.encode(this.password))
            .userName(this.userName)
            .platformType(Member.PlatformType.LOCAL)
            .build();
  }

  public Member toEntity(PasswordEncoder passwordEncoder, Member.PlatformType platformType) {
    return Member.builder()
            .email(this.email)
            .password(passwordEncoder.encode(this.password))
            .userName(this.userName)
            .platformType(platformType)
            .build();
  }
}
