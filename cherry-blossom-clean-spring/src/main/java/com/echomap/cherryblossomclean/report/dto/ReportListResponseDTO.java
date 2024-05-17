package com.echomap.cherryblossomclean.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "관리자 전체 제보 조회 응답 DTO")
public class ReportListResponseDTO {

    @Schema(description = "에러 메시지")
    private String error;

    @Schema(description = "전체 제보 리스트")
    private List<ReportDetailResponseDTO> reports;
}
