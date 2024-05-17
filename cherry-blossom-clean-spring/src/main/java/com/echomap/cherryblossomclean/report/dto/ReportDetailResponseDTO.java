package com.echomap.cherryblossomclean.report.dto;

import com.echomap.cherryblossomclean.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "제보 상세내역 응답 DTO")
public class ReportDetailResponseDTO {

    @Schema(description = "제보 종류", example = "CHERRY or TRASH")
    private String category;

    @Schema(description = "제보 내용", example = "OOO역 n번 출구 앞 일반쓰레기통")
    private String content;

    @Schema(description = "제보자 이메일", example = "example@email.com")
    private String email;

    @Schema(description = "제보일자", example = "0000-00-00 00:00:00")
    private String reportDate;

    @Schema(description = "제보 고유 ID", example = "79cfd801-ce62-4d6a-b18b-d57e79ef1aa5")
    private String reportId;

    @Schema(description = "제보 처리 상태", example = "true or false")
    private boolean status;

    @Schema(description = "처리일자", example = "0000-00-00 00:00:00")
    private String updatedAt;

    public ReportDetailResponseDTO(Report report) {
        this.category = String.valueOf(report.getCategory());
        this.content = report.getContent();
        this.email = report.getMember().getEmail();
        this.reportDate = report.getReportDate().toString();
        this.reportId = report.getId();
        this.status = report.isStatus();
        this.updatedAt = report.getUpdatedAt().toString();
    }
}
