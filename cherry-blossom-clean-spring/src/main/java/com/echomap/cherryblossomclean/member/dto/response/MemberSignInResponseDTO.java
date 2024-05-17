package com.echomap.cherryblossomclean.member.dto.response;


import com.echomap.cherryblossomclean.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 응답 DTO")
public class MemberSignInResponseDTO {

    @Schema(description = "회원 이메일", example = "example@email.com")
    private String email;

    @Schema(description = "회원 이름", example = "홍길동")
    private String userName;

    @Schema(description = "가입 날짜", example = "0000-00-00 00:00:00")
    private LocalDate joinDate;

    @Schema(description = "JWT 인증 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQ09NTU9OIiwiZW1haWwiOiJ5b25nc2VvcDAxQG5hdmVyLmNvbSIsImlzcyI6IuyatOyYgeyekCIsImlhdCI6MTcxNTgyNDY3OCwiZXhwIjoxNzE1OTExMDc4LCJzdWIiOiJ5b25nc2VvcDAxQG5hdmVyLmNvbSJ9.6CUXLM8tllUVlUv-MU_3Y4dD0fwUOKIsFwFidEc7u-yJYugaym0WTCwMXIss88xo4o7mi_Qo3cyg89bIo8Uh4g")
    private String token;

    @Schema(description = "회원 권한", example = "ADMIN, COMMON")
    private String role;

    public MemberSignInResponseDTO(Member member, String token) {
        this.email = member.getEmail();
        this.userName = member.getUserName();
        this.joinDate = LocalDate.from(member.getJoinDate());
        this.token = token;
        this.role = member.getRole().toString();
    }
}
