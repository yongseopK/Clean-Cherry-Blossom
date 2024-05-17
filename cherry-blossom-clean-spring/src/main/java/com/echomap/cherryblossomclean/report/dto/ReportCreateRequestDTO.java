package com.echomap.cherryblossomclean.report.dto;

import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.report.entity.Report;
import com.echomap.cherryblossomclean.report.entity.ReportCategory;
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
@Schema(description = "제보 등록 요청 DTO")
public class ReportCreateRequestDTO {

    @NotBlank
    @Schema(description = "제보 종류", example = "CHERRY or TRASH")
    private String category;

    @NotBlank
    @Schema(description = "제보 내용", example = "OOO역 n번 출구 앞 일반쓰레기통")
    private String content;

    public Report toEntity(Member member) {
        return Report.builder()
                .category(ReportCategory.valueOf(category))
                .content(content)
                .member(member)
                .build();
    }
}
