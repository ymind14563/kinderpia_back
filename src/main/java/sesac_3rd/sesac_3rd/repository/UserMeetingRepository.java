package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.entity.UserMeeting;

import java.util.Optional;


public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    boolean existsByUser_UserIdAndMeeting_MeetingId(Long userId, Long meetingId);

    // 모임 상세조회
    @Query("SELECT um FROM UserMeeting um " +
            "JOIN um.meeting m " +
            "WHERE m.meetingId = :meetingId")
    Optional<UserMeeting> findByMeetingId(@Param("meetingId") Long meetingId);
}
