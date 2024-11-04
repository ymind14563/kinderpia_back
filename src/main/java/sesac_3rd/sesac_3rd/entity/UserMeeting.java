package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserMeeting")
@Entity
public class UserMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usermeeting_id", nullable = false)
    private Long userMeetingId;  // 매핑아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 유저아이디 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;  // 모임아이디 (FK)

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;  // 차단여부 (기본값 FALSE)

    @Column(name = "is_accepted")
    private Boolean isAccepted = null;  // 수락여부 (NULL: 대기 중)

    @Column(name = "is_withdraw", nullable = false)
    private Boolean isWithdraw = false;  // 탈퇴여부 (기본값 FALSE)

    @Column(name = "capacity", nullable = false)
    private int capacity; // 참가 인원 (기본값 1)

    @Column(name = "blockedAt")
    private LocalDateTime blockedAt;  // 차단일자

    @Column(name = "acceptedAt")
    private LocalDateTime acceptedAt;  // 수락일자

    @Column(name = "withdrawAt")
    private LocalDateTime withdrawAt;  // 탈퇴일자
}
