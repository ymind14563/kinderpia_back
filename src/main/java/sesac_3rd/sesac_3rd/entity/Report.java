package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Report")
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long reportId;  // 신고아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatmsg_id")
    private ChatMessage chatMessage;  // 채팅메세지아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;  // 리뷰아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;  // 모임아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;  // 신고자아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)
    private User reported;  // 피신고자아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_rs_id")
    private ReportReason reportReason;  // 신고사유아이디 (FK)

    @Column(name = "reportmsg_content")
    private String reportMessageContent;  // 신고내용

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;  // 생성일자
}
