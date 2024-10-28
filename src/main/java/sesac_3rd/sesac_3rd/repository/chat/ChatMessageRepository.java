package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomChatroomId(Long chatRoomId);
}
