package sesac_3rd.sesac_3rd.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeetingStatusDTO {
    private Long userId;
    private Long meetingId;
}
