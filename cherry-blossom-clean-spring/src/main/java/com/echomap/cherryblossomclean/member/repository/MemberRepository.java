package com.echomap.cherryblossomclean.member.repository;

import com.echomap.cherryblossomclean.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.status = true AND m.updatedAt < :expirationTime")
    void deleteByStatusTrueAndUpdatedAtBefore(@Param("expirationTime") LocalDateTime expriationTime);

    List<Member> findByStatusTrueAndUpdatedAtBefore(LocalDateTime expirationTime);

    List<Member> findByIsWithdrawalRequestedTrueAndWithdrawalDateBefore(LocalDateTime expirationTime);


}
