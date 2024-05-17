package com.echomap.cherryblossomclean.member.controller;

import com.echomap.cherryblossomclean.auth.TokenUserInfo;
import com.echomap.cherryblossomclean.exception.ForcedWithdrawalException;
import com.echomap.cherryblossomclean.exception.ValidateEmailException;
import com.echomap.cherryblossomclean.member.dto.request.MemberModifyRequestDTO;
import com.echomap.cherryblossomclean.member.dto.request.MemberSignInRequestDTO;
import com.echomap.cherryblossomclean.member.dto.request.MemberSignUpRequsetDTO;
import com.echomap.cherryblossomclean.member.dto.response.*;
import com.echomap.cherryblossomclean.member.service.AuthService;
import com.echomap.cherryblossomclean.member.service.MemberService;
import com.echomap.cherryblossomclean.member.service.SocialLoginSevice;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Map;

@Tag(name="회원 관리")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;
  private final AuthService authService;
  private final SocialLoginSevice socialLoginSevice;

  @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다. 이메일, 비밀번호, 이름을 입력해야 합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "회원가입 성공",
      content = @Content(schema = @Schema(implementation = MemberSignUpResponseDTO.class)))
  @PostMapping()
  public ResponseEntity<?> register(
          @Parameter(description = "회원 가입 요청 데이터") @Validated @RequestBody MemberSignUpRequsetDTO dto, BindingResult result) {

    log.info("member POST!! - {}", dto);

    if (result.hasErrors()) {
      log.warn(result.toString());
      return ResponseEntity.badRequest().body(result.getFieldError());
    }

    try {
      MemberSignUpResponseDTO responseDTO = memberService.create(dto);
      return ResponseEntity.ok().body(responseDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
    } catch (Exception e) {
      log.error("An unknown error occurred.", e);
      return ResponseEntity.internalServerError().body("알 수 없는 에러가 발생했습니다.");
    }
  }

  @Operation(summary = "이메일 중복 확인", description = "입력된 이메일이 기존 회원과 중복되는지 확인합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "이메일 중복 여부",
      content = @Content(schema = @Schema(implementation = Boolean.class)))
  @GetMapping("/check")
  public ResponseEntity<?> checkEmail(String email) {
    try {
      boolean flag = memberService.isDuplicateEmail(email);
      log.warn("{} 중복여부 - {}", email, flag);
      return ResponseEntity.ok().body(flag);
    } catch (ValidateEmailException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력하여 로그인합니다. 성공 시 JWT 토큰이 발급됩니다.")
  @ApiResponse(
      responseCode = "200",
      description = "로그인 성공",
      content = @Content(schema = @Schema(implementation = MemberSignInResponseDTO.class)))
  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody MemberSignInRequestDTO dto) {
    //log.info("dto : {}", dto);
    try {
      MemberSignInResponseDTO responseDTO = authService.authenticate(dto);
      return ResponseEntity.ok().body(responseDTO);
    } catch (ForcedWithdrawalException e) {
      //log.warn(e.getMessage());
      return ResponseEntity.status(450).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "회원 정보 수정", description = "로그인한 회원의 정보(비밀번호, 이름)를 수정합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "정보수정 성공",
      content = @Content(schema = @Schema(implementation = MemberModifyResponseDTO.class)))
  @PutMapping("/modify")
  public ResponseEntity<?> modifyMember(
      @RequestBody MemberModifyRequestDTO requestDTO,
      @AuthenticationPrincipal TokenUserInfo userInfo) {
    try {
      MemberModifyResponseDTO responseDTO = memberService.modify(requestDTO, userInfo);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "내 정보 조회", description = "로그인한 회원의 정보를 조회합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "정보조회 성공",
      content = @Content(schema = @Schema(implementation = MemberInfoResponseDTO.class)))
  @GetMapping("/my-info")
  public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal TokenUserInfo userInfo) {

    try {
      MemberInfoResponseDTO info = authService.findUserInfo(userInfo);
      return ResponseEntity.ok().body(info);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "전체 회원 조회", description = "관리자 권한으로 전체 회원 정보를 조회합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "회원 전체조회 성공",
      content = @Content(schema = @Schema(implementation = MemberListResponseDTO.class)))
  @GetMapping("/admin")
  public ResponseEntity<?> findAllMembers(@AuthenticationPrincipal TokenUserInfo userInfo) {
    try {
      MemberListResponseDTO allMembers = memberService.getAllMembers(userInfo);
      return ResponseEntity.ok().body(allMembers);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "회원 강제 탈퇴 처리/취소", description = "관리자 권한으로 특정 회원을 강제 탈퇴/탈퇴 취소시킵니다.")
  @ApiResponse(responseCode = "200", description = "강제 탈퇴 처리/취소 완료")
  @PatchMapping("/admin/{email}")
  public ResponseEntity<?> deleteMember(
      @PathVariable(name = "email") String email, @AuthenticationPrincipal TokenUserInfo userInfo) {
    try {
      memberService.updateMember(email, userInfo);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
      return new ResponseEntity<>("처리가 완료됐습니다.", headers, HttpStatus.OK);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "회원 역할 변경", description = "관리자 권한으로 특정 회원의 역할(일반 회원 <-> 관리자)을 변경합니다.")
  @ApiResponse(responseCode = "200", description = "역할 변경 성공")
  @PatchMapping("/admin/promote/{email}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> changeToRole(
      @AuthenticationPrincipal TokenUserInfo userInfo, @PathVariable(name = "email") String email) {

    try {
      memberService.changeToRole(userInfo, email);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
      return new ResponseEntity<>("처리가 완료됐습니다.", headers, HttpStatus.OK);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "회원 탈퇴 요청", description = "로그인한 회원의 탈퇴를 요청합니다.")
  @ApiResponse(responseCode = "200", description = "탈퇴요청 성공")
  @PostMapping("/withdrawal")
  public ResponseEntity<?> requestWithdrawal(@AuthenticationPrincipal TokenUserInfo userInfo) {
    try {
      memberService.requestWithdrawal(userInfo);
      return ResponseEntity.ok().body("회원탈퇴 요청이 완료되었습니다.");
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "회원 탈퇴 취소", description = "탈퇴 요청 상태에서 탈퇴를 취소하고 재로그인합니다.")
  @ApiResponse(responseCode = "200", description = "탈퇴 취소요청 성공")
  @PostMapping("/withdrawal/cancel")
  public ResponseEntity<?> cancelWithdrawal(@RequestBody MemberSignInRequestDTO dto) {
    try {
      memberService.cancelWithdrawal(dto);
      MemberSignInResponseDTO responseDTO = authService.authenticate(dto);
      return ResponseEntity.ok().body(responseDTO);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "토큰 유효성 검사", description = "현재 JWT 토큰의 유효성을 검사합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "토큰 유효성 검증 성공",
      content = @Content(schema = @Schema(implementation = Boolean.class)))
  @PostMapping("/token/validate")
  public ResponseEntity<?> validateToken(@AuthenticationPrincipal TokenUserInfo userInfo) {
    try {
      boolean isValid = authService.validateToken(userInfo);
      if (isValid) {
        return ResponseEntity.ok().build();
      }
    } catch (NullPointerException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("알 수 없는 오류가 발생했습니다.");
    }
    return null;
  }

  @Operation(summary = "구글 로그인", description = "구글 계정으로 로그인하여 JWT 토큰을 발급받습니다.")
  @ApiResponse(
      responseCode = "200",
      description = "구글 로그인 성공",
      content = @Content(schema = @Schema(implementation = MemberSignInResponseDTO.class)))
  @PostMapping("/google")
  public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
    String credential = request.get("credential");

    try {
      MemberSignInResponseDTO dto = socialLoginSevice.googleLoginHandler(credential);
      return ResponseEntity.ok().body(dto);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "네이버 로그인", description = "네이버 계정으로 로그인하여 JWT 토큰을 발급받습니다.")
  @ApiResponse(
      responseCode = "200",
      description = "네이버 로그인 성공",
      content = @Content(schema = @Schema(implementation = MemberSignInResponseDTO.class)))
  @PostMapping("/naver")
  public ResponseEntity<?> naverLogin(@RequestBody Map<String, String> token) {

    String credential = token.get("token");
    try {
      MemberSignInResponseDTO responseDTO = socialLoginSevice.naverLoginHandler(credential);
      return ResponseEntity.ok().body(responseDTO);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "비밀번호 찾기", description = "입력한 이메일로 임시 비밀번호를 전송합니다.")
  @ApiResponse(responseCode = "200", description = "비밀번호 재발급 성공")
  @PostMapping("/forget-password")
  public ResponseEntity<?> forgetPassword(String email) {
    try {
      MemberModifyResponseDTO dto = memberService.sendTemporaryPassword(email);
      return ResponseEntity.ok().body(dto);
    } catch (RuntimeException e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
