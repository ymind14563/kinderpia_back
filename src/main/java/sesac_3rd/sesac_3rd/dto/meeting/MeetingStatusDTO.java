package sesac_3rd.sesac_3rd.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 모임 상태
public class MeetingStatusDTO {
    private Long meetingId;
    private MeetingStatus meetingStatus;
}
