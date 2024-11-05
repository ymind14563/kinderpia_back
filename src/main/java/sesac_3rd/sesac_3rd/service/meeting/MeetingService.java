package sesac_3rd.sesac_3rd.service.meeting;

import org.springframework.data.domain.Pageable;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

public interface MeetingService {
    // 모임 목록 (default - 최신순 정렬)
    PaginationResponseDTO<MeetingDTO> getAllMeetings(Pageable pageable);

    // 모임 목록 (open - 열려있는것만 정렬)
    PaginationResponseDTO<MeetingDTO> getOpenMeetings(Pageable pageable);

    // 키워드로 타이틀과 장소 검색
    PaginationResponseDTO<MeetingDTO> searchMeetingsByKeyword(String keyword, Pageable pageable);

    // 특정 ID 로 모임 상세조회 (모임장 정보 포함)
    MeetingDetailDTO getDetailMeeting(Long meetingId);

    // 모임 생성
    Meeting createMeeting(Long userId, MeetingFormDTO meetingFormDTO);

    // 모임 수정
    void updateMeeting(Long userId, Long meetingId, MeetingFormDTO meetingFormDTO);

    // 모임 삭제
    Boolean deleteMeeting(Long userId, Long meetingId);
}
