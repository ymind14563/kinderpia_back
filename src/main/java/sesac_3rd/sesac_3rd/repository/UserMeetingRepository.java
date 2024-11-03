package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.entity.UserMeeting;

import java.util.List;
import java.util.Optional;


public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    boolean existsByUser_UserIdAndMeeting_MeetingId(Long userId, Long meetingId); // 참가한 사용자 여부 확인

    List<UserMeeting> findByMeeting_MeetingId(Long meetingId);

    List<UserMeeting> findByUser_UserIdAndIsAcceptedTrue(Long userId);

    boolean existsByUser_UserIdAndMeeting_MeetingIdAndIsAcceptedTrue(Long userId, Long meetingId);


}
