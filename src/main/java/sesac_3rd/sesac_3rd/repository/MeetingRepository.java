package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.entity.Meeting;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    // 모임 목록 (open - 열려있는것만 정렬)
    Page<Meeting> findByMeetingStatus(MeetingStatus status, Pageable pageable);

    // 키워드로 타이틀, 장소 검색
    @Query("SELECT m FROM Meeting m JOIN m.place p " +
            "WHERE m.meetingTitle LIKE %:keyword OR p.location LIKE %:keyword%")
    Page<Meeting> findByMeetingTitleOrLocation(@Param("keyword") String keyword, Pageable pageable);

    // 모임 상세조회 (모임장 정보 포함)
    @Query("SELECT m FROM Meeting m JOIN m.user u WHERE m.meetingId = :meetingId")
    Optional<Meeting> findByMeetingIdWithUser(@Param("meetingId") Long meetingId);
}
