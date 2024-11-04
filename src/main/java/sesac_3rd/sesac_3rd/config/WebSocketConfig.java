package sesac_3rd.sesac_3rd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*
chatMessageService 에서 아래와 같이
private final SimpMessagingTemplate messagingTemplate;
messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getChatroomId(), responseMessage);
@SendTo를 사용하지 않고 직접적으로 메세지 전송을 위한 설정 파일 (@SendTo 만 사용하면 굳이 필요없는 파일임)

@SendTo를 사용하면 WebSocketConfig 설정 없이도 Spring이 기본적으로 내부에서 메시지 브로커를 활성화하고
브로커를 통해 메시지를 라우팅해주기 때문에, 설정 파일 없이도 메시지 전송이 가능

특정 전송 경로나 복잡한 라우팅 규칙을 설정하거나 SockJS 같은 기능을 추가하려는 경우에는 WebSocketConfig 설정이 필요함
 */

/* 참고: 채팅방에 관련하여 관례적인 표현법
topic: 구독자들이 동일한 경로에 대해 구독(수신)하면, 하나의 메시지를 여러 구독자가 수신가능 (Pub-Sub 방식)
queue: 특정 클라이언트만 메시지를 받을 수 있게 설계되어서, 1:1 채팅처럼 발신자와 수신자 사이에 독점적으로 메시지를 주고받는 경우 (Point-to-Point 방식)
 */

@Configuration // WebSocket 설정을 위한 클래스
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화하여 STOMP 기반의 메시지 처리
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트로 메세지를 보내기 위한 경로에 대해 메시지 브로커 설정
        config.enableSimpleBroker("/topic"); // "/topic" 경로를 구독하는 클라이언트에게 메시지 전송
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지를 전송할 때 "/app"으로 시작하는 경로를 사용
        /*
        * ChatController 에서 @MessageMapping로 경로를 지정하면
            @MessageMapping("/chatroom/{chatroomId}/chatmsg")
            @SendTo("/topic/chatroom/{chatroomId}")

        * React 에서 서버에 메세지 전송 시 프리픽스 '/app' 을 사용
            stompClient.send("/app/chatroom/123/chatmsg", {}, JSON.stringify({
                senderId: 1,
                messageContent: "Hello World"
            }));
         */
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       /*
        STOMP 엔드포인트 등록 - 클라이언트가 WebSocket 연결을 생성할 때 사용하는 엔드포인트
        서버가 "/ws" 엔드포인트를 통해 WebSocket 을 활성화해주면 (= registry.addEndpoint("/ws")) STOMP 프로토콜을 사용하여 클라이언트와 실시간 통신 가능
        */
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
             /*
                 "/ws" 경로로 클라이언트가 연결 요청을 보낼 수 있도록 설정

                 React 에서 SockJS와 Stomp 클라이언트로 WebSocket 연결 설정 시
                 클라이언트는 아래와 같이 "/ws" 엔드포인트를 통해 서버에 연결 요청을 보냄
                 const stompClient = Stomp.over(new SockJS('/ws'));
                 stompClient.connect({}, function() { ... });
             */

                .withSockJS(); // SockJS 지원을 추가하여 WebSocket 을 지원하지 않는 환경에서도 HTTP 로 폴링하여 통신 가능하게 함
    }
}
