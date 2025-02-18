package sesac_3rd.sesac_3rd.repository.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;

import java.time.Instant;
import java.util.List;
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
        // 메시지가 Redis에 저장된 시간이 기준이라 정확한 순서 보장이 어려움 (db에 저장된 메세지 생성 시간이 필요함)
//        double score = Instant.now().toEpochMilli();
        // db에 저장된 메세지 생성 시간이 필요함
        double score = message.getCreatedAtTimestamp();

        redisTemplate.opsForZSet().add(key, jsonMessage, score);

        // TTL 설정
        redisTemplate.expire(key, java.time.Duration.ofDays(7));
    }

    // 메시지 조회
    // zrange 로 pagination 처럼 적용
    public List<ChatMessageDTO.ChatMessage> getMessagesFromRedis(Long chatroomId, int page, int size) {
        String key = CHATROOM_KEY_PREFIX + chatroomId + ":messages";
        int start = (page - 1) * size;
        int end = start + size - 1;

        Set<String> jsonMessages = redisTemplate.opsForZSet().range(key, start, end);
        return jsonMessages != null ? jsonMessages.stream()
                .map(this::deserialize)
                .collect(Collectors.toList()) : List.of();
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
