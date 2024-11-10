package sesac_3rd.sesac_3rd.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatNotificationDTO;
import sesac_3rd.sesac_3rd.entity.ChatNotification;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.chat.ChatNotificationMapper;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatNotificationRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomStatusRepository;

@Service
@RequiredArgsConstructor
public class ChatNotificationService {

    private final ChatRoomStatusRepository chatRoomStatusRepository;
    private final ChatNotificationRepository chatNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatNotificationMapper chatNotificationMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;



    // 새로운 메시지 도착 시 사용자가 채팅방에 없을 경우
    public void unreadNotification(Long chatroomId, Long userId) {

        // ChatRoom 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

        // User 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        // 사용자가 채팅방에 있는지 여부 확인
        if (!chatRoomStatusRepository.isUserInChatRoom(chatroomId, userId)) {

            // 안 읽은 메시지 수 증가
            ChatNotification updateUnreadCount = incrementUnreadCount(chatRoom, user);

            ChatNotificationDTO chatNotificationDTO = chatNotificationMapper.ChatNotificationtoChatNotificationDTO(updateUnreadCount, chatRoom);

            messagingTemplate.convertAndSend("/topic/chatroom/notification", chatNotificationDTO);
        }
    }



    // 안 읽은 메시지 수를 1 증가시키고 저장
    private ChatNotification incrementUnreadCount(ChatRoom chatRoom, User user) {
        ChatNotification chatNotification = chatNotificationRepository
                .findByUserAndChatRoom(chatRoom, user)
                .orElseGet(() -> chatNotificationMapper.chatNotificationDTOtoEntity(chatRoom, user));

        // unreadCount 증가
        chatNotification.setUnreadCount(chatNotification.getUnreadCount() + 1);

        return chatNotificationRepository.save(chatNotification);
    }

    // 안 읽은 메시지 수 초기화
    public void resetUnreadCount(ChatRoom chatRoom, User user) {
        ChatNotification chatNotification = chatNotificationRepository
                .findByUserAndChatRoom(chatRoom, user)
                .orElseGet(() -> chatNotificationMapper.chatNotificationDTOtoEntity(chatRoom, user));

        // unreadCount 초기화
        chatNotification.setUnreadCount(0);

        chatNotificationRepository.save(chatNotification);
    }
}