package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.entity.Meeting;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    // 모임 목록 (open - 열려있는것만 정렬)
    List<Meeting> findByMeetingStatus(MeetingStatus status, Sort sort);

    // 키워드로 타이틀, 장소 검색
    List<Meeting> findByMeetingTitleContainingOrMeetingLocationContaining(String titleKeyword, String locationKeyword, Sort sort);

}
