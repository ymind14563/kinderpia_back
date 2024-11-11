package sesac_3rd.sesac_3rd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // RedisTemplate 을 빈으로 정의하여 Redis 와의 데이터 작업을 쉽게 처리할 수 있도록 함
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // RedisTemplate 인스턴스를 생성
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Redis 연결을 위한 ConnectionFactory 를 설정
        template.setConnectionFactory(redisConnectionFactory);

        // Redis 에 저장될 키를 문자열로 직렬화 설정
        template.setKeySerializer(new StringRedisSerializer());

        // Redis 에 저장될 값을 문자열로 직렬화 설정
        template.setValueSerializer(new StringRedisSerializer());

        // 완성된 RedisTemplate 을 빈으로 반환하여 사용 가능하게 함
        return template;
    }
}
