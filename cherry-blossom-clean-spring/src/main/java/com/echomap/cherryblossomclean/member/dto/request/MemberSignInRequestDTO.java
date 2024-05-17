package com.echomap.cherryblossomclean.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 요청 DTO")
public class MemberSignInRequestDTO {

    @NotBlank
    @Schema(description = "회원 이메일", example = "example@email.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "test1234!")
    private String password;

    @Schema(description = "자동 로그인 여부", example = "true/false")
    private boolean autoLogin;
}
