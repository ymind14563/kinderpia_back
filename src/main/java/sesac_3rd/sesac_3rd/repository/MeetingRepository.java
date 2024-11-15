package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.entity.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    // 모임 목록 (open - 열려있는것만 정렬)
    Page<Meeting> findByMeetingStatus(MeetingStatus status, Pageable pageable);

    // 모임 목록 (더보기: 모임 시간순으로 정렬 + deleted 제외)
    @Query("SELECT m FROM Meeting m " +
            "WHERE m.meetingStatus != 'DELETED' ")
    Page<Meeting> findByMeetingStatusNotDeleted(Pageable pageable);

    // 키워드로 타이틀, 장소 검색
    @Query("SELECT m FROM Meeting m " +
            "WHERE m.meetingTitle LIKE %:keyword% OR m.district LIKE %:keyword%")
    Page<Meeting> findByMeetingTitleOrDistrict(@Param("keyword") String keyword, Pageable pageable);

    // 모임 상세조회 (profile_img, chatroom_id 포함)
    @Query("SELECT m FROM Meeting m " +
            "JOIN m.user u " +
            "JOIN ChatRoom cr ON m.meetingId = cr.meeting.meetingId " +
            "WHERE m.meetingId = :meetingId")
    Optional<Meeting> findByMeetingIdWithUserAndChatRoom(@Param("meetingId") Long meetingId);

    // 특정 사용자의 진행중 모임 목록 조회
    @Query("SELECT m FROM Meeting m WHERE m.user.userId = :userId AND m.meetingStatus = 'ONGOING'")
    List<Meeting> findOngoingMeetingsByUserId(@Param("userId") Long userId);

    // 총 모임 수('모집중' 수)
    long countByMeetingStatus(MeetingStatus meetingStatus);

    // 카테고리별 모임 수('삭제'된 모임 제외)
    @Query("SELECT " +
            "m.meetingCategory.meetingCtgId as categoryId, " +
            "m.meetingCategory.meetingCtgName as categoryName, " +
            "COUNT(m) as meetingCount " +
            "FROM Meeting m " +
            "WHERE m.meetingStatus != 'DELETED' " +
            "GROUP BY m.meetingCategory.meetingCtgId, m.meetingCategory.meetingCtgName")
    List<Object[]> countMeetingsByCategory();
}
