package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sesac_3rd.sesac_3rd.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN Meeting m ON cr.meeting.meetingId = m.meetingId JOIN UserMeeting um ON um.meeting.meetingId = m.meetingId WHERE um.user.userId = :userId")
    Page<ChatRoom> findByUserId(Long userId, Pageable pageable);
}
