package com.echomap.cherryblossomclean.report.entity;

import com.echomap.cherryblossomclean.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_report")
public class Report {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id")
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportCategory category;

  @Column(nullable = false)
  private String content;

  @CreationTimestamp
  private LocalDateTime reportDate;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
  private Member member;

  @Column
  private boolean status;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
