package com.echomap.cherryblossomclean.report.scheduler;

import com.echomap.cherryblossomclean.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportCleanupScheduler {
    private final ReportRepository reportRepository;

    @Scheduled(cron = "0 */10 * * * *") // 10분 마다 스케쥴러 실행
    //@Scheduled(fixedRate = 30000)
    public void deleteExpireReports() {
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        //LocalDateTime expirationTime = LocalDateTime.now().minusSeconds(30);
        reportRepository.deleteByStatusTrueAndUpdatedAtBefore(expirationTime);
    }

}
