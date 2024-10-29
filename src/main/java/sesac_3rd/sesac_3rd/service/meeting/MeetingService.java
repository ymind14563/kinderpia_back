package sesac_3rd.sesac_3rd.service.meeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

import java.util.List;

public interface MeetingService {
    // 모임 목록 (default - 최신순 정렬)
    PaginationResponseDTO<MeetingDTO> getAllMeetings(Pageable pageable);

    // 모임 목록 (open - 열려있는것만 정렬)
    PaginationResponseDTO<MeetingDTO> getOpenMeetings(Pageable pageable);

    // 키워드로 타이틀과 장소 검색
    PaginationResponseDTO<MeetingDTO> searchMeetingsByKeyword(String keyword, Pageable pageable);
}
