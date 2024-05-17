package com.echomap.cherryblossomclean.report.controller;

import com.echomap.cherryblossomclean.auth.TokenUserInfo;
import com.echomap.cherryblossomclean.report.dto.ReportCreateRequestDTO;
import com.echomap.cherryblossomclean.report.dto.ReportListResponseDTO;
import com.echomap.cherryblossomclean.report.service.ReportService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "제보 관리")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

  private final ReportService reportService;

  @Operation(summary = "제보 등록", description = "새로운 제보를 DB에 등록합니다. JWT토큰과 카테고리, 내용 정보가 필요합니다.")
  @ApiResponse(responseCode = "200", description = "제보 등록 성공")
  @PostMapping
  public ResponseEntity<?> reportRegistration(
      @AuthenticationPrincipal TokenUserInfo userInfo,
      @Validated @RequestBody ReportCreateRequestDTO dto,
      BindingResult result) {

    if (result.hasErrors()) {
      log.warn("DTO 검증 에러 : {}", result.getFieldError());
      return ResponseEntity.badRequest().body(result.getFieldError());
    }

    try {
      reportService.create(dto, userInfo);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError()
          .body(ReportListResponseDTO.builder().error(e.getMessage()).build());
    }
  }

  @Operation(summary = "제보 내역 조회", description = "로그인한 회원의 제보 내역을 조회합니다. JWT토큰이 필요합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "조회 성공",
      content = @Content(schema = @Schema(implementation = ReportListResponseDTO.class)))
  @GetMapping
  public ResponseEntity<?> getReport(@AuthenticationPrincipal TokenUserInfo userInfo) {
    ReportListResponseDTO retrieve = reportService.retrieve(userInfo);

    return ResponseEntity.ok().body(retrieve);
  }

  @Operation(summary = "전체 조회 조회", description = "관리자 권한으로 모든 회원의 제보를 조회합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "제보 전체조회 성공",
      content = @Content(schema = @Schema(implementation = ReportListResponseDTO.class)))
  @GetMapping("/admin")
  public ResponseEntity<?> getAllReport(@AuthenticationPrincipal TokenUserInfo userInfo)
      throws RuntimeException {

    try {
      ReportListResponseDTO retrieve = reportService.adminRetrieve(userInfo);

      return ResponseEntity.ok().body(retrieve);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      e.printStackTrace();
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(
      summary = "제보 처리 요청",
      description = "관리자가 제보에 대한 내용을 처리했을때 요청하는 API입니다. 제보 Id와 JWT토큰이 필요합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "제보 처리 성공",
      content = @Content(schema = @Schema(implementation = String.class)))
  @PatchMapping("/admin/{reportId}")
  public ResponseEntity<?> deleteReport(
      @PathVariable(name = "reportId") String reportId,
      @AuthenticationPrincipal TokenUserInfo userInfo) {
    //UUID uuidReportId = UUID.fromString(reportId);

    try {
      reportService.updateReport(reportId, userInfo);
      return ResponseEntity.ok().body("처리가 완료되었습니다!");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("에러가 발생했습니다.");
    }
  }
}
