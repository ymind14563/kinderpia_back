package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
