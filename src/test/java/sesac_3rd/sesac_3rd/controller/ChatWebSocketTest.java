package sesac_3rd.sesac_3rd.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChatWebSocketTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    public void testSendMessage() throws Exception {
        // 테스트용 사용자 생성
        Long testUserId = 1L;
        User testUser = User.builder()
                .userId(testUserId)
                .nickname("TestUser")
                .build();

        String jwt = tokenProvider.create(testUser);
        if (jwt == null || jwt.isEmpty()) {
            throw new RuntimeException("JWT 생성 실패: 토큰이 비어 있음");
        }

        // SockJS 클라이언트 구성
        List<Transport> transports = List.of(new WebSocketTransport(new org.springframework.web.socket.client.standard.StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        // 직접 MappingJackson2MessageConverter를 생성하고, findAndRegisterModules()를 통해 JavaTimeModule 등 등록
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.getObjectMapper().findAndRegisterModules();
        stompClient.setMessageConverter(messageConverter);

        CountDownLatch latch = new CountDownLatch(1);

        // 핸드쉐이크 헤더: 쿠키와 Origin 설정 (JWT 검증에 사용)
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.put("Cookie", Collections.singletonList("jwt=" + jwt));
        handshakeHeaders.add("Origin", "http://localhost:3000");

        String url = "ws://localhost:" + port + "/ws";
        StompSession session = stompClient.connect(url, handshakeHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                // WebSocket 연결 오류 로그 출력
                System.err.println("WebSocket 전송 에러: " + exception.getMessage());
            }
        }).get(5, TimeUnit.SECONDS);

        // 구독 경로 등록 (Controller의 @SendTo 경로와 일치)
        session.subscribe("/topic/chatroom/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("수신: " + payload);
                latch.countDown();
            }
        });

        // 전송할 메시지 DTO 생성
        ChatMessageDTO.ChatMessage message = ChatMessageDTO.ChatMessage.builder()
                .senderId(testUserId)
                .chatmsgContent("테스트 메시지")
                .createdAt(LocalDateTime.now())
                .build();

        // 메시지 전송 헤더에 Authorization 헤더 추가
        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/app/chatroom/1/chatmsg");
        sendHeaders.add("Authorization", "Bearer " + jwt);

        session.send(sendHeaders, message);

        Thread.sleep(1000);

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("메시지 응답 수신 실패");
        }
    }
}
