package sesac_3rd.sesac_3rd.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final TokenProvider tokenProvider;

    public WebSocketHandshakeInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 헤더에서 쿠키 추출
        String token = null;
        if (request.getHeaders().containsKey("Cookie")) {
            for (String cookie : request.getHeaders().get("Cookie")) {
                System.out.println("Received Cookie: " + cookie);
                // 쿠키 이름이 "jwt="로 시작하는 경우 추출
                if (cookie.contains("jwt=")) {
                    token = cookie.substring(cookie.indexOf("jwt=") + 4);
                    token = token.split(";")[0]; // 여러 쿠키가 포함된 경우 첫 번째 값만 추출
                    break;
                }
            }
        }

        if (token != null) {
            // 토큰을 검증하고 사용자 ID 추출
            String userId = tokenProvider.validateAndGetUserId(token);
            System.out.println("Extracted userId: " + userId);
            if (userId != null) {
                attributes.put("userId", userId); // WebSocket 세션에 userId 저장
            } else {
                System.out.println("Invalid token, handshake rejected.");
                // 토큰 검증 실패 시 연결을 끊음
                return false;
            }
        } else {
            System.out.println("JWT cookie not found, handshake rejected.");
            // 토큰이 없으면 연결 거부
            return false;
        }

        // JWT가 있으면 연결을 진행
        return true;
    }



    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // 후처리 작업
    }
}
