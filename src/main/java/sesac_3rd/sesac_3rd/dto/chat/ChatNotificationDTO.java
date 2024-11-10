package sesac_3rd.sesac_3rd.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotificationDTO {

    private Long chatroomId;
    private String lastMessageContent;
    private int unreadCount;
    private LocalDateTime lastMessageCreatedAt;
}