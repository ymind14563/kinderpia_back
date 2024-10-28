package sesac_3rd.sesac_3rd.service.meeting;

import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;

import java.util.List;

public interface MeetingService {
    // 모임 목록 (default - 최신순 정렬)
    List<MeetingDTO> getAllMeetings();

    // 모임 목록 (open - 열려있는것만 정렬)
    List<MeetingDTO> getOpenMeetings();

    // 키워드로 타이틀과 장소 검색
    List<MeetingDTO> searchMeetingsByKeyword(String keyword);
}
