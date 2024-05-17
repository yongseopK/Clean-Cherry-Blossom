package com.echomap.cherryblossomclean.member.dto.request;

import com.echomap.cherryblossomclean.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "정보수정 요청 DTO")
public class MemberModifyRequestDTO {

    @Schema(description = "수정 비밀번호", example = "test1234!")
    private String password;

    @Schema(description = "회원 이름", example = "홍길동")
    private String userName;

    // 비밀번호를 수정할때 재인코딩해서 엔터티화
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .password(passwordEncoder.encode(this.password))
                .userName(this.userName)
                .build();
    }

    // 비밀번호를 수정하지 않을때 원래의 비밀번호로 덮어쓰기위함
    public Member toEntity() {
        return Member.builder()
                .password(this.password)
                .userName(this.userName)
                .build();
    }
}
