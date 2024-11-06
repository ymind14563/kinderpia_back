package sesac_3rd.sesac_3rd.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeetingTimeListDTO {
    private Long meetingId;  // 모임아이디
    private String meetingTitle;  // 모임명
    private LocalDateTime meetingTime;   // 모임일시
}
