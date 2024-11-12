package sesac_3rd.sesac_3rd.service.chat;

import jakarta.transaction.Transactional;
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

import java.util.Optional;

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
    @Transactional
    public void unreadNotification(Long chatroomId, Long userId) {
        System.out.println("unreadNotification - chatroomId: " + chatroomId + ", userId: " + userId);

        // Redis에서 사용자가 채팅방에 없는지 확인
        if (!chatRoomStatusRepository.isUserInChatRoom(chatroomId, userId)) {
            // 알림이 있는지 확인
            Optional<ChatNotification> existingNotification = chatNotificationRepository.findByUserAndChatRoom(userId, chatroomId);

            if (existingNotification.isPresent()) {
                // 알림이 이미 존재하면 안 읽은 메시지 수만 증가
                chatNotificationRepository.incrementUnreadCount(userId, chatroomId);
            } else {
                // 알림이 없으면 새로 생성
                ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                        .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

                ChatNotification newNotification = chatNotificationMapper.chatNotificationDTOtoEntity(chatRoom, user);

                System.out.println("새 알림 생성: " + newNotification);

                chatNotificationRepository.save(newNotification);
            }

            // 최신 알림 정보 가져오기
            ChatNotificationDTO chatNotificationDTO = chatNotificationRepository
                    .findLatestNotificationInfo(userId, chatroomId)
                    .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

            System.out.println("최신 알림 DTO: " + chatNotificationDTO);

            // 메시지 전송
            messagingTemplate.convertAndSend("/topic/chatroom/notification", chatNotificationDTO);
        }
    }






//    // 안 읽은 메시지 수를 1 증가시키고 저장 // Lazy Loading 문제로 쿼리에서 직접 관리로 변경
//    private ChatNotification incrementUnreadCount(ChatRoom chatRoom, User user) {
//        ChatNotification chatNotification = chatNotificationRepository
//                .findByUserAndChatRoom(chatRoom, user)
//                .orElseGet(() -> chatNotificationMapper.chatNotificationDTOtoEntity(chatRoom, user));
//
//        // unreadCount 증가
//        chatNotification.setUnreadCount(chatNotification.getUnreadCount() + 1);
//
//        return chatNotificationRepository.save(chatNotification);
//    }

//    // 안 읽은 메시지 수 초기화 // Lazy Loading 문제로 쿼리에서 직접 관리로 변경
//    public void resetUnreadCount(ChatRoom chatRoom, User user) {
//        ChatNotification chatNotification = chatNotificationRepository
//                .findByUserAndChatRoom(chatRoom, user)
//                .orElseGet(() -> chatNotificationMapper.chatNotificationDTOtoEntity(chatRoom, user));
//
//        // unreadCount 초기화
//        chatNotification.setUnreadCount(0);
//
//        chatNotificationRepository.save(chatNotification);
//    }

    // 안 읽은 메시지 수 초기화
    public void resetUnreadCount(Long chatroomId, Long userId) {
        chatNotificationRepository.resetUnreadCount(chatroomId, userId);
    }
}