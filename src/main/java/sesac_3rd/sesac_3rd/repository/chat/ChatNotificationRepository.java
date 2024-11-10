package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.ChatNotification;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;

import java.util.Optional;

@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {
    Optional<ChatNotification> findByUserAndChatRoom(ChatRoom chatRoom, User user);
}
