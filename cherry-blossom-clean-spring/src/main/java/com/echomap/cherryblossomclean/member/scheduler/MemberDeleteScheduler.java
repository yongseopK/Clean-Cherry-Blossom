package com.echomap.cherryblossomclean.member.scheduler;

import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.member.repository.MemberRepository;
import com.echomap.cherryblossomclean.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberDeleteScheduler {

    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Scheduled(cron = "0 */10 * * * *") // 10분 마다 스케쥴러 실행
    //@Scheduled(fixedRate = 30000)
    @Transactional
    public void deleteExpireReports() {
        log.info("스케쥴러 실행");
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        //LocalDateTime expirationTime = LocalDateTime.now().minusSeconds(30);
        List<Member> expiredMembers = memberRepository.findByStatusTrueAndUpdatedAtBefore(expirationTime);

        for (Member member : expiredMembers) {
            reportRepository.deleteByMember(member);

            memberRepository.delete(member);
        }
        //memberRepository.deleteByStatusTrueAndUpdatedAtBefore(expirationTime);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteWithdrawnMembers() {
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(7);
        List<Member> expiredMembers = memberRepository.findByIsWithdrawalRequestedTrueAndWithdrawalDateBefore(expirationDate);

        for (Member member : expiredMembers) {
            reportRepository.deleteByMember(member);
            memberRepository.delete(member);
        }
    }
}
