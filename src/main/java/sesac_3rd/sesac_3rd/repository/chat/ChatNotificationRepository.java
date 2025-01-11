package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.chat.ChatNotificationDTO;
import sesac_3rd.sesac_3rd.entity.ChatNotification;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;

import java.util.Optional;

@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {

    Optional<ChatNotification> findByUserAndChatRoom(ChatRoom chatRoom, User user);

    // 이미 존재하는 알림을 찾거나 없으면 새로 생성하는 메서드를 서비스에서 처리
    @Query("SELECT cn FROM ChatNotification cn WHERE cn.user.id = :userId AND cn.chatRoom.id = :chatroomId")
    Optional<ChatNotification> findByUserAndChatRoom(@Param("userId") Long userId, @Param("chatroomId") Long chatroomId);

    // 최신 메시지 정보와 안 읽은 메시지 수를 포함한 ChatNotificationDTO를 반환
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.chat.ChatNotificationDTO(cn.chatRoom.id, cm.chatmsgContent, cn.unreadCount, cm.createdAt) " +
            "FROM ChatNotification cn " +
            "JOIN cn.chatRoom cr " +
            "JOIN cr.chatMessages cm " +
            "WHERE cn.user.id = :userId AND cr.id = :chatroomId " +
            "ORDER BY cm.createdAt DESC")
    Optional<ChatNotificationDTO> findLatestNotificationInfo(@Param("userId") Long userId, @Param("chatroomId") Long chatroomId);



    // 안 읽은 메시지 수를 1 증가
    @Modifying
    @Query("UPDATE ChatNotification cn SET cn.unreadCount = cn.unreadCount + 1 " +
            "WHERE cn.user.id = :userId AND cn.chatRoom.id = :chatroomId")
    void incrementUnreadCount(@Param("userId") Long userId, @Param("chatroomId") Long chatroomId);



    // 안 읽은 메시지 수를 0으로 초기화
    @Modifying
    @Query("UPDATE ChatNotification cn SET cn.unreadCount = 0 " +
            "WHERE cn.user.id = :userId AND cn.chatRoom.id = :chatroomId")
    void resetUnreadCount(@Param("userId") Long userId, @Param("chatroomId") Long chatroomId);
}
