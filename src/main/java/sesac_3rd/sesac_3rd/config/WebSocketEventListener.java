package sesac_3rd.sesac_3rd.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomStatusRepository;
import sesac_3rd.sesac_3rd.service.chat.ChatNotificationService;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatNotificationService chatNotificationService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private ChatRoomStatusRepository chatRoomStatusRepository;
    private TokenProvider tokenProvider;

    /**
     * WebSocket 구독 이벤트를 처리
     * 사용자가 특정 채팅방을 구독할 때, 해당 채팅방의 읽지 않은 메시지 수를 초기화
     * 구독 이벤트로, 클라이언트가 WebSocket 구독을 요청할 때 발생
     */
    @EventListener
    public void WebSocketSubscribeListener(SessionSubscribeEvent event) {
        // StompHeaderAccessor로 이벤트 메시지에서 헤더 정보를 가져옴
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination(); // 구독 목적지 경로 ("/topic/chatroom/{chatroomId}" 또는 "/topic/chatroom/notification")
        System.out.println("destination>>>>"  + destination);

        if (destination != null) {
            Long chatroomId = extractChatroomId(destination);

            // 알림 구독 경로인지 확인
            if (chatroomId == null && destination.endsWith("notification")) {
                // 알림 경로일 경우 특별히 처리할 내용이 있다면 여기에 추가
                System.out.println("알림 구독 경로로 연결됨.");
            } else if (chatroomId != null) {
                // 채팅방 구독 경로일 경우 chatroomId를 사용하여 처리
                // 헤더에서 사용자 ID 추출
                Long userId = getUserIdFromHeader(headerAccessor);

                System.out.println(userId + "사용자가  " + chatroomId + "번 채팅방 구독 경로로 연결됨.");
                if (chatroomId != null && userId != null) {
                    // Redis에 입장 상태 기록
                    chatRoomStatusRepository.enterChatRoom(chatroomId, userId);
                    System.out.println("<<<<<User " + userId + " entered chatroom " + chatroomId);

                    chatNotificationService.resetUnreadCount(chatroomId, userId);
                    System.out.println("안 읽은 메세지 수 초기화");
                }
            }
        }
    }


    @EventListener
    public void WebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        // WebSocket 구독 해제 이벤트 처리 (채팅방에서 퇴장한 경우)
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();

        if (destination != null && destination.startsWith("/topic/chatroom/")) {
            Long chatroomId = extractChatroomId(destination);
            Long userId = getUserIdFromHeader(headerAccessor);

            // 사용자 퇴장 처리
            if (chatroomId != null && userId != null) {
                // Redis에서 퇴장 상태 삭제
                chatRoomStatusRepository.exitChatRoom(chatroomId, userId);
                System.out.println("User " + userId + " exited chatroom " + chatroomId);
            }
        }
    }


    /**
     * 구독 목적지 경로에서 채팅방 ID를 추출
     * "/topic/chatroom/123" 경로에서 "123"을 추출하여 Long 타입으로 반환
     */
    private Long extractChatroomId(String destination) {
        String[] parts = destination.split("/");
        String lastPart = parts[parts.length - 1];

        try {
            Long chatroomId = Long.parseLong(lastPart);

            System.out.println("추출된 chatroomId: " + chatroomId);

            return chatroomId;
        } catch (NumberFormatException e) {
            // chatroomId 처럼 숫자가 아닌 경우 null 반환
            return null;
        }
    }


    /**
     * 사용자 ID는 STOMP "Authorization" 헤더에 포함된 JWT 에서 추출
     */
    private Long getUserIdFromHeader(StompHeaderAccessor headerAccessor) {
        // Authorization 헤더에서 "Bearer "로 시작하는 부분을 추출하여 JWT 토큰을 가져옵니다.
        String authorizationHeader = headerAccessor.getFirstNativeHeader("Authorization");
        System.out.println("authorizationHeader >>>" + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // JWT 토큰 부분을 추출
            String token = authorizationHeader.substring(7);  // "Bearer "를 제외한 부분을 추출
            System.out.println("token>>>>>"+ token);
            // tokenProvider를 사용해 토큰에서 userId를 추출
            String userId = tokenProvider.validateAndGetUserId(token);
            System.out.println("userId>>>>>"+ userId);
            // userId가 존재하면 Long 타입으로 변환하여 반환, 없으면 null
            return userId != null ? Long.parseLong(userId) : null;
        }

        return null;  // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 null 반환
    }


}
