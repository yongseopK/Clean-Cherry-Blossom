package com.echomap.cherryblossomclean.member.dto.response;

import com.echomap.cherryblossomclean.report.dto.ReportDetailResponseDTO;
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
@Schema(description = "관리자 전체회원 조회 응답 DTO")
public class MemberListResponseDTO {

    @Schema(description = "에러 메시지")
    private String error;

    @Schema(description = "회원 리스트")
    private List<MemberInfoResponseDTO> members;
}
