package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.user.UserMeetingListDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

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

    // 사용자 리뷰 목록 조회(장소 포함)
    @Query("SELECT r FROM Review r JOIN FETCH r.place WHERE r.user.userId = :userId")
    List<Review> findReviewListByUserIdWithPlace(@Param("userId") Long userId);

    // 사용자 리뷰 목록 조회 - 페이징이 필요한 경우 (fetch join과 함께 사용)
//    @Query(value = "SELECT r FROM Review r JOIN FETCH r.place WHERE r.user.userId = :userId",
//            countQuery = "SELECT COUNT(r) FROM Review r WHERE r.user.userId = :userId")
//    Page<Review> findReviewsByUserIdWithPlace(@Param("userId") Long userId, Pageable pageable);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인지 아닌지 필터링 요청 api 분리) - 페이지네이션

//    SELECT m.*
//    FROM user_meeting um
//    JOIN meeting m ON um.meeting_id = m.meeting_id
//    WHERE um.user_id = 2 /* 특정 사용자 ID */
//    AND (um.is_leader = 1 OR um.is_accepted = 1)
//    AND um.is_withdraw = 0 /* 탈퇴하지 않은 */
//    AND um.is_blocked = 0  /* 차단되지 않은 */
//    AND m.meeting_status IN ('ONGOING', 'COMPLETED', 'END') /* DELETED 상태 제외 */
//    ORDER BY m.created_at DESC /* 정렬 기준 (최신순으로 가정) */
//    LIMIT 3  /* 페이지당 데이터 수 */
//    OFFSET 0;  /* (페이지 번호 - 1) * 페이지당 데이터 수 */

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
//    PaginationResponseDTO<UserMeetingListDTO> getUserLeaderMeetingList(Long userId, int size, int page);
    @Query("SELECT m, u.nickname, u.profileImg, c.meetingCtgName FROM Meeting m JOIN m.user u JOIN m.meetingCategory c WHERE u.userId = :userId AND m.meetingStatus IN :validStatus")
    Page<Meeting> findByUserId(@Param("userId") Long userId, @Param("validStatus") List<MeetingStatus> validStatus, Pageable pageable);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
//    PaginationResponseDTO<UserMeetingListDTO> getUserMeetingList(Long userId, int size, int page);

}
