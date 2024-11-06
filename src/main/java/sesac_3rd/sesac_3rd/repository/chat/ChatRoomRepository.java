package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN Meeting m ON cr.meeting.meetingId = m.meetingId JOIN UserMeeting um ON um.meeting.meetingId = m.meetingId WHERE um.user.userId = :userId")
    Page<ChatRoom> findByUserId(Long userId, Pageable pageable);

    boolean existsByMeeting_MeetingId(Long meetingId);

    Page<ChatRoom> findByMeeting_MeetingIdIn(List<Long> meetingIds, Pageable pageable);

    // chatroomId 의 meetingId 찾기
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.meeting.meetingId = :meetingId")
    ChatRoom findByMeetingId(@Param("meetingId") Long meetingId);
}
