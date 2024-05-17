package com.echomap.cherryblossomclean.report.service;

import com.echomap.cherryblossomclean.auth.TokenUserInfo;
import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.member.repository.MemberRepository;
import com.echomap.cherryblossomclean.report.dto.ReportCreateRequestDTO;
import com.echomap.cherryblossomclean.report.dto.ReportDetailResponseDTO;
import com.echomap.cherryblossomclean.report.dto.ReportListResponseDTO;
import com.echomap.cherryblossomclean.report.entity.Report;
import com.echomap.cherryblossomclean.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReportService {

  private final ReportRepository reportRepository;
  private final MemberRepository memberRepository;

  // 제보 등록
  public void create(ReportCreateRequestDTO dto, TokenUserInfo userInfo) {
    Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());


    foundMember.ifPresent(member -> {
      Report report = reportRepository.save(dto.toEntity(member));

      member.addReport(report);
    });

    log.info("성공적으로 제보가 완료되었습니다. 내용 : {}", dto.getContent());
  }

  public ReportListResponseDTO retrieve(TokenUserInfo userInfo) {

    Member member = memberRepository.findByEmail(userInfo.getEmail()).orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다."));

    List<Report> reportList = member.getReportList();

    List<ReportDetailResponseDTO> dtoList = reportList.stream()
            .map(ReportDetailResponseDTO::new)
            .toList();
    return ReportListResponseDTO.builder().reports(dtoList).build();
  }

  public ReportListResponseDTO adminRetrieve(TokenUserInfo userInfo) {

    Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());

    foundMember.ifPresent(member -> {

      if(member.getRole() != Member.Role.ADMIN) {
        throw new RuntimeException("운영자만 시도할 수 있는 요청입니다.");
      }
    });

    List<Report> all = reportRepository.findAll();
      List<ReportDetailResponseDTO> dtoList = all.stream()
              .map(ReportDetailResponseDTO::new)
              .toList();

      return ReportListResponseDTO.builder().reports(dtoList).build();
  }

  public void deleteReport(String reportId, TokenUserInfo userInfo) {

    Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());

    foundMember.ifPresent(member -> {
      if(member.getRole() != Member.Role.ADMIN) {
        throw new RuntimeException("운영자만 시도할 수 있는 요청입니다.");
      }
    });

    Report report = reportRepository.findById(reportId).orElseThrow(() -> new RuntimeException("정보를 찾을 수 없습니다."));

    reportRepository.delete(report);
  }

  public void updateReport(String reportId, TokenUserInfo userInfo) {
    Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());

    foundMember.ifPresent(member -> {
      if(member.getRole() != Member.Role.ADMIN) {
        throw new RuntimeException("운영자만 시도할 수 있는 요청입니다.");
      }

      Optional<Report> foundReport = reportRepository.findById(reportId);

      foundReport.ifPresent(report -> {
        if(!report.isStatus()) {
          report.setStatus(true);
          report.setUpdatedAt(LocalDateTime.now());
          reportRepository.save(report);
        } else {
          throw new RuntimeException("이미 처리된 제보입니다.");
        }
      });

      if(foundReport.isEmpty()) {
        throw new RuntimeException("해당 제보를 찾을 수 없습니다.");
      }
    });

    if(foundMember.isEmpty()) {
      throw new RuntimeException("운영자를 찾을 수 없습니다.");
    }
  }
}
