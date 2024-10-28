package sesac_3rd.sesac_3rd.service.meeting;

import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;

import java.util.List;

public interface MeetingService {
    // 모임 리스트 (전체)
    List<MeetingDTO> getAllMeetings();
}
