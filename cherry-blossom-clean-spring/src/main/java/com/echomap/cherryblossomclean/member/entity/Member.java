package com.echomap.cherryblossomclean.member.entity;

import com.echomap.cherryblossomclean.report.entity.Report;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.echomap.cherryblossomclean.member.entity.Member.Role.COMMON;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "reportList")
@Table(name = "tbl_member")
public class Member {

  @Id
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String userName;

  @CreationTimestamp
  private LocalDateTime joinDate;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = COMMON;

  @OneToMany(
      mappedBy = "member",
      orphanRemoval = true)
  private List<Report> reportList = new ArrayList<>();

  @Column private boolean status;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "withdrawal_date")
  private LocalDateTime withdrawalDate;

  @Column(name = "is_withdrawal_requested")
  private boolean isWithdrawalRequested;

  @Enumerated(EnumType.STRING)
  @Column(name = "platform_type")
  private PlatformType platformType;

  public int getReportCount() {
    return reportList.size();
  }

  public void addReport(Report report) {
    this.reportList.add(report);
    report.setMember(this);
  }

  public enum Role {
    ADMIN,
    COMMON;
  }

  public enum PlatformType {
    LOCAL,
    GOOGLE,
    NAVER,
  }
}
