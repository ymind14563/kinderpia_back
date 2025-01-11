package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.manager.DailyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.manager.MonthlyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.user.UserMeetingListDTO;
import sesac_3rd.sesac_3rd.dto.user.UserMeetingTimeListDTO;
import sesac_3rd.sesac_3rd.dto.user.UserReviewDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    // 아이디로 사용자 조회
    User findByLoginId(String loginId);

    // 아이디 중복 확인
    boolean existsByLoginId(String loginId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 전화번호 중복 확인
    boolean existsByPhoneNum(String phoneNum);

    User findByUserId(Long userId);

    // 사용자 리뷰 목록 조회(장소 포함)
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.user.UserReviewDTO(" +
            "r.reviewId, " +
            "r.reviewContent, " +
            "r.star, " +
            "r.createdAt, " +
            "(SELECT COUNT(l) FROM Likes l WHERE l.review.id = r.reviewId), " +
            "p.placeId, " +
            "p.placeName) " +
            "FROM Review r " +
            "JOIN r.place p " +
            "WHERE r.user.userId = :userId AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    Page<UserReviewDTO> findReviewListByUserIdWithPlace(@Param("userId") Long userId, Pageable pageable);


    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
    @Query("SELECT m, u.nickname, u.profileImg, c.meetingCtgName FROM Meeting m JOIN m.user u JOIN m.meetingCategory c WHERE u.userId = :userId AND m.meetingStatus IN :validStatus ORDER BY m.meetingTime DESC")
    Page<Meeting> meetingFindByLeaderUserId(@Param("userId") Long userId, @Param("validStatus") List<MeetingStatus> validStatus, Pageable pageable);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
    @Query("SELECT DISTINCT new sesac_3rd.sesac_3rd.dto.user.UserMeetingListDTO( " +
            "m.meetingId, " +
            "m.meetingTitle, " +
            "m.meetingStatus, " +
            "m.meetingTime, " +
            "mc.meetingCtgName, " +
            "m.meetingLocation, " +
            "m.capacity, " +
            "m.totalCapacity, " +
            "m.createdAt, " +
            "u.nickname, " +
            "u.profileImg, " +
            "CASE WHEN m.user.userId = :userId THEN true ELSE false END) " +
            "FROM Meeting m " +
            "LEFT JOIN UserMeeting um ON um.meeting.meetingId = m.meetingId " +
            "LEFT JOIN User u ON m.user.userId = u.userId " +
            "LEFT JOIN MeetingCategory mc ON m.meetingCategory.meetingCtgId = mc.meetingCtgId " +
            "WHERE (m.user.userId = :userId " +
            "    OR (um.user.userId = :userId " +
            "        AND um.isWithdraw = false " +
            "        AND um.isAccepted = true " +
            "        AND um.isBlocked = false)) " +
            "AND m.meetingStatus IN :validStatus ORDER BY m.meetingTime DESC")
    Page<UserMeetingListDTO> meetingFindByUserId(@Param("userId") Long userId, @Param("validStatus") List<MeetingStatus> validStatus, Pageable pageable);

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)
    @Query("SELECT DISTINCT new sesac_3rd.sesac_3rd.dto.user.UserMeetingTimeListDTO( " +
            "m.meetingId, " +
            "m.meetingTitle, " +
            "m.meetingTime) " +
            "FROM Meeting m " +
            "LEFT JOIN UserMeeting um ON um.meeting.meetingId = m.meetingId " +
            "WHERE (m.user.userId = :userId " +
            "    OR (um.user.userId = :userId " +
            "        AND um.isWithdraw = false " +
            "        AND um.isAccepted = true " +
            "        AND um.isBlocked = false)) " +
            "AND m.meetingStatus IN :validStatus")
    List<UserMeetingTimeListDTO> meetingTimeFindByUserId(@Param("userId") Long userId, @Param("validStatus") List<MeetingStatus> validStatus);

    // 승인 대기자 목록(각각 모임에 대한)
    @Query("SELECT um.user FROM UserMeeting um WHERE um.meeting.meetingId = :meetingId AND um.isAccepted IS NULL")
    List<User> getUserMeetingApprovalList(@Param("meetingId") Long meetingId);

    // 탈퇴하지 않은 사용자 수 조회
    long countByIsDeletedFalse();

    // 월별 사용자 가입 수
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.manager.MonthlyUserStatisticsDTO( " +
            "MONTH(u.createdAt), COUNT(u)) " +
            "FROM User u " +
            "WHERE u.isDeleted = false " +
            "AND YEAR(u.createdAt) = :year " +
            "AND MONTH(u.createdAt) <= MONTH(CURRENT_TIMESTAMP) " +
            "GROUP BY MONTH(u.createdAt) " +
            "ORDER BY MONTH(u.createdAt)")
    List<MonthlyUserStatisticsDTO> findMonthlyStatByYear(@Param("year") int year);

    // 일별 사용자 가입 수
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.manager.DailyUserStatisticsDTO(" +
            "DAY(u.createdAt), COUNT(u)) " +
            "FROM User u " +
            "WHERE u.isDeleted = false " +
            "AND YEAR(u.createdAt) = :year " +
            "AND MONTH(u.createdAt) = :month " +
            "AND u.createdAt <= CURRENT_TIMESTAMP " +
            "GROUP BY DAY(u.createdAt) " +
            "ORDER BY DAY(u.createdAt)")
    List<DailyUserStatisticsDTO> findDailyStatByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
