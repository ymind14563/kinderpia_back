package sesac_3rd.sesac_3rd.mapper.chat;

import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatNotificationDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.ChatMessage;
import sesac_3rd.sesac_3rd.entity.ChatNotification;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;

import java.util.Comparator;

@Component
public class ChatNotificationMapper {

    // 가장 최근 메시지 내용 및 시간
    public static ChatRoomDTO.LastMessageInfo getLastMessageInfo(ChatRoom chatRoom) {
        return chatRoom.getChatMessages().stream()
                .max(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(chatMessage -> new ChatRoomDTO.LastMessageInfo(
                        chatMessage.getChatmsgContent(),
                        chatMessage.getCreatedAt()))
                .orElse(new ChatRoomDTO.LastMessageInfo("아직 작성한 메세지가 없어요.", null));

    }


    public ChatNotificationDTO ChatNotificationtoChatNotificationDTO(ChatNotification chatNotification, ChatRoom chatRoom) {
        ChatRoomDTO.LastMessageInfo lastMessageInfo = getLastMessageInfo(chatRoom);

        return ChatNotificationDTO.builder()
                .chatroomId(chatNotification.getChatRoom().getChatroomId())
                .lastMessageContent(lastMessageInfo.getMessageContent())
                .unreadCount(chatNotification.getUnreadCount())
                .lastMessageCreatedAt(lastMessageInfo.getCreatedAt())
                .build();

    }


    public ChatNotification chatNotificationDTOtoEntity(ChatRoom chatRoom, User user) {
        ChatNotification chatNotification = new ChatNotification();
        chatNotification.setUser(user);
        chatNotification.setChatRoom(chatRoom);
        chatNotification.setUnreadCount(0);

        return chatNotification;
    }
}