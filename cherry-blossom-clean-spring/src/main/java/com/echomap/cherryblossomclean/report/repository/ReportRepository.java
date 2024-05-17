package com.echomap.cherryblossomclean.report.repository;

import com.echomap.cherryblossomclean.member.entity.Member;
import com.echomap.cherryblossomclean.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    @Query("SELECT r FROM Report r WHERE r.member=?1")
    List<Report> findAllByMember(Member member);

    @Modifying
    @Transactional
    @Query("DELETE FROM Report r WHERE r.status = true AND r.updatedAt < :expirationTime")
    void deleteByStatusTrueAndUpdatedAtBefore(@Param("expirationTime") LocalDateTime expriationTime);

    @Transactional
    void deleteByMember(Member member);

}

