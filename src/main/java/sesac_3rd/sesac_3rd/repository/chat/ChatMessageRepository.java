package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomChatroomId(Long chatroomId, Sort sort);

    @Query("SELECT c FROM ChatMessage c " +
            "WHERE c.chatRoom.chatroomId = :chatroomId " +
            "AND c.createdAt < :createdAt " +
            "ORDER BY c.createdAt DESC")
    List<ChatMessage> findOlderMessages(
            @Param("chatroomId") Long chatroomId,
            @Param("createdAt") LocalDateTime createdAt,
            Pageable pageable);



//    List<ChatMessage> findByChatRoomChatroomIdAndCreatedAtLessThanOrderByCreatedAtDesc(
//            Long chatroomId, LocalDateTime createdAt, Pageable pageable);

}
