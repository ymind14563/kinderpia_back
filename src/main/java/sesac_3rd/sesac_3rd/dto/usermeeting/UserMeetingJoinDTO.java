package sesac_3rd.sesac_3rd.dto.usermeeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 모임 참가
public class UserMeetingJoinDTO {
    private Long userId;
    private Long meetingId;
    private int capacity;

    private boolean isBlocked; // 차단여부 (기본값 FALSE)
    private boolean isAccepted; // 수락여부 (NULL: 대기 중)
    private boolean isWithdraw; // 탈퇴여부 (기본값 FALSE)
    private LocalDateTime blockedAt; // 차단일자
    private LocalDateTime acceptedAt; // 수락일자
    private LocalDateTime withdrawAt; // 탈퇴일자
}
