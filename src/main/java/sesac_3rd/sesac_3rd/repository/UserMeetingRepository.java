package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.UserMeeting;


public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    boolean existsByUser_UserIdAndMeeting_MeetingId(Long userId, Long meetingId);
}
