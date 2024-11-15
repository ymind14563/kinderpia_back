package sesac_3rd.sesac_3rd.service.meeting;

import org.springframework.data.domain.Pageable;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingStatusDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

public interface MeetingService {
    // 모임 목록 (default - 최신순 정렬)
    PaginationResponseDTO<MeetingDTO> getAllMeetings(Pageable pageable);

    // 모임 목록 (open - 열려있는것만 정렬 + 모임 시간순으로 정렬)
    PaginationResponseDTO<MeetingDTO> getOpenMeetings(Pageable pageable);

    // 키워드로 타이틀과 장소 검색 (모임 시간순으로 정렬)
    PaginationResponseDTO<MeetingDTO> searchMeetingsByKeyword(String keyword, Pageable pageable);

    // 모임 상세조회 (profile_img, chatroom_id 포함)
    MeetingDetailDTO getDetailMeeting(Long meetingId);

    // 모임 생성
    Meeting createMeeting(Long userId, MeetingFormDTO meetingFormDTO);

    // 모임 수정
    Boolean updateMeeting(Long userId, Long meetingId, MeetingFormDTO meetingFormDTO);

    // 모임삭제 (모임장이 삭제, 관리자가 삭제 : DELETED)
    Boolean deletedMeeting(Long userId, Long meetingId);

    // 모집완료 (인원마감, 모임장이 임의로 마감 : COMPLETED)
    Boolean completedMeeting(Long userId, Long meetingId);

    // meetingStatus 상태 확인
    MeetingStatusDTO meetingStatus(Long meetingId);
}