package com.echomap.cherryblossomclean.member.dto.response;

import com.echomap.cherryblossomclean.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 응답 DTO")
public class MemberSignUpResponseDTO {

  @Schema(description = "회원 이메일", example = "example@email.com")
  private String email;

  @Schema(description = "회원 이름", example = "홍길동")
  private String userName;

  @Schema(description = "가입 날짜", example = "2024-01-01 00:00:00")
  @JsonProperty("join-date")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime joinDate;

  public MemberSignUpResponseDTO(Member member) {
    this.email = member.getEmail();
    this.userName = member.getUserName();
    this.joinDate = member.getJoinDate();
  }
}
