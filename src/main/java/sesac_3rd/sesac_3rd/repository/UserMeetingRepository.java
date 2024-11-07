package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.entity.UserMeeting;

import java.util.List;
import java.util.Optional;


public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    // 사용자가 모임에 참가중인지 확인
    boolean existsByUser_UserIdAndMeeting_MeetingId(Long userId, Long meetingId);
    // 특정 모임과 가입자에 대한 UserMeeting entity 찾기
    Optional<UserMeeting> findByUser_UserIdAndMeeting_MeetingId(Long joinUserId, Long meetingId);

    List<UserMeeting> findByMeeting_MeetingId(Long meetingId);

    List<UserMeeting> findByUser_UserIdAndIsAcceptedTrue(Long userId);

    boolean existsByUser_UserIdAndMeeting_MeetingIdAndIsAcceptedTrue(Long userId, Long meetingId);

    // 일별 모임 참여자 수(수락된 사람들)
    @Query("SELECT FUNCTION('DATE', um.acceptedAt) as date, COUNT(um) " +
            "FROM UserMeeting um " +
            "WHERE um.isAccepted = true " +
            "AND um.acceptedAt IS NOT NULL " +
            "GROUP BY FUNCTION('DATE', um.acceptedAt) " +
            "ORDER BY FUNCTION('DATE', um.acceptedAt) ASC")
    List<Object[]> countDailyAcceptedUsers();
}
