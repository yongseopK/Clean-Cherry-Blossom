package com.echomap.cherryblossomclean.map.controller;

import com.echomap.cherryblossomclean.map.entity.GarbageCan;
import com.echomap.cherryblossomclean.map.service.GarbageCanService;
import com.echomap.cherryblossomclean.member.dto.response.MemberSignUpResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "지도 관리")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/maps")
public class MapController {

  private final GarbageCanService garbageCanService;

  @Operation(summary = "쓰레기통 위치 조회", description = "선택한 지역구의 쓰레기통 위치를 반환합니다.")
  @ApiResponse(responseCode = "200", description = "쓰레기통 위치 요청 성공", content = @Content(schema = @Schema(implementation = GarbageCan.class)))
  @GetMapping("/garbage-can")
  public ResponseEntity<?> getGarbageCanLocation(String value) {
    List<GarbageCan> garbageCanLocations = garbageCanService.getGarbageCanLocations(value);
    return ResponseEntity.ok().body(garbageCanLocations);
  }
}
