package sesac_3rd.sesac_3rd.repository.chat;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomStatusRepository {
    // Redis에서 Pub/Sub 기능을 사용하거나 Redis 템플릿을 직접 조작해야 한다면, 클래스를 사용하여 Redis 작업을 처리하는 것이 일반적

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOpsEnterInfo;

    public ChatRoomStatusRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOpsEnterInfo = redisTemplate.opsForHash();
    }

    public void enterChatRoom(Long chatroomId, Long userId) {
        String key = chatroomId.toString();
        String field = userId.toString();
        String value = Boolean.TRUE.toString();
        hashOpsEnterInfo.put(key, field, value);
        System.out.println("사용자 " + userId + "님 채팅방에 입장, 번호: " + chatroomId);
    }

    public void exitChatRoom(Long chatroomId, Long userId) {
        String key = chatroomId.toString();
        String field = userId.toString();
        hashOpsEnterInfo.delete(key, field);
        System.out.println("사용자 " + userId + "님 채팅방에 퇴장, 번호: " + chatroomId);
    }

    public boolean isUserInChatRoom(Long chatroomId, Long userId) {
        String key = chatroomId.toString(); // redis 는 문자열 기반
        String field = userId.toString();
        return hashOpsEnterInfo.hasKey(key, field);

    }
}
