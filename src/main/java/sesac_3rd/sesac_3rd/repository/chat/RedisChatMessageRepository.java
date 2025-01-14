package sesac_3rd.sesac_3rd.repository.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisChatMessageRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CHATROOM_KEY_PREFIX = "chatroom:";

    // 메시지 저장
    public void saveMessageToRedis(Long chatroomId, ChatMessageDTO.ChatMessage message) {
        String key = CHATROOM_KEY_PREFIX + chatroomId + ":messages";

        // 메시지를 JSON 문자열로 직렬화
        String jsonMessage = serialize(message);

       // createdAt을 Unix Timestamp로 변환하여 Redis Sorted Set의 Score로 저장
        double score = Instant.now().toEpochMilli();

        redisTemplate.opsForZSet().add(key, jsonMessage, score);

        // TTL 설정
        redisTemplate.expire(key, java.time.Duration.ofDays(7));
    }

    // 메시지 조회
    public Set<ChatMessageDTO.ChatMessage> getMessagesFromRedis(Long chatroomId) {
        String key = CHATROOM_KEY_PREFIX + chatroomId + ":messages";
        Set<String> jsonMessages = redisTemplate.opsForZSet().range(key, 0, -1);
        return jsonMessages != null ? jsonMessages.stream()
                .map(this::deserialize)
                .collect(Collectors.toSet()) : Set.of();
    }

    // 직렬화 (객체 -> JSON)
    private String serialize(ChatMessageDTO.ChatMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Redis 직렬화 실패", e);
        }
    }

    // 역직렬화 (JSON -> 객체)
    private ChatMessageDTO.ChatMessage deserialize(String json) {
        try {
            return objectMapper.readValue(json, ChatMessageDTO.ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Redis 역직렬화 실패", e);
        }
    }
}
