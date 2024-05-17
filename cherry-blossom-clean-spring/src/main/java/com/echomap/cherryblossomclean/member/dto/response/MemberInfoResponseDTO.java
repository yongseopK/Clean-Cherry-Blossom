package com.echomap.cherryblossomclean.member.dto.response;

import com.echomap.cherryblossomclean.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "정보조회 응답 DTO")
public class MemberInfoResponseDTO {

    @Schema(description = "회원 이메일", example = "example@email.com")
    private String email;

    @Schema(description = "회원 이름", example = "홍길동")
    private String userName;

    @Schema(description = "가입 날짜", example = "0000-00-00 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    @Schema(description = "제보 횟수", example = "0")
    private int reportCount;

    @Schema(description = "회원 권한", example = "ADMIN, COMMON")
    private Member.Role role;

    @Schema(description = "탈퇴 대기 여부", example = "true/false")
    private boolean status;

    @Schema(description = "탈퇴 신청 날짜", example = "0000-00-00 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "가입 플랫폼", example = "LOCAL, GOOGLE, NAVER")
    private String platform;

    public MemberInfoResponseDTO(Member member) {
        this.email = member.getEmail();
        this.userName = member.getUserName();
        this.joinDate = member.getJoinDate();
        this.reportCount = member.getReportCount();
        this.role = member.getRole();
        this.status = member.isStatus();
        this.updatedAt = member.getUpdatedAt();
        this.platform = member.getPlatformType().toString();
    }
}
