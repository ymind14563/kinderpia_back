package sesac_3rd.sesac_3rd.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.service.chat.ChatNotificationService;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatNotificationService chatNotificationService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /**
     * WebSocket 구독 이벤트를 처리
     * 사용자가 특정 채팅방을 구독할 때, 해당 채팅방의 읽지 않은 메시지 수를 초기화
     * 구독 이벤트로, 클라이언트가 WebSocket 구독을 요청할 때 발생
     */
    @EventListener
    public void WebSocketSubscribeListener(SessionSubscribeEvent event) {
        // StompHeaderAccessor로 이벤트 메시지에서 헤더 정보를 가져옴
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination(); // 구독 목적지 경로 ("/topic/chatroom/{chatroomId}")

        // 채팅방 구독 경로인지 확인
        if (destination != null && destination.startsWith("/topic/chatroom/")) {
            // 경로에서 채팅방 ID 추출
            Long chatroomId = extractChatroomId(destination);
            // 헤더에서 사용자 ID 추출
            Long userId = getUserIdFromHeader(headerAccessor);

            if (chatroomId != null && userId != null) {
                ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                        .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));


                chatNotificationService.resetUnreadCount(chatRoom, user);

            }
        }
    }

    /**
     * 구독 목적지 경로에서 채팅방 ID를 추출
     * "/topic/chatroom/123" 경로에서 "123"을 추출하여 Long 타입으로 반환
     */
    private Long extractChatroomId(String destination) {
        String[] parts = destination.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    /**
     * StompHeaderAccessor에서 사용자 ID를 추출
     * 사용자 ID는 "userId"라는 이름의 헤더에 저장되어 있음
     */
    private Long getUserIdFromHeader(StompHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getFirstNativeHeader("userId");

        // userId가 존재하면 Long 타입으로 변환하여 반환, 없으면 null
        return userId != null ? Long.parseLong(userId) : null;
    }
}
