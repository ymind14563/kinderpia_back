package sesac_3rd.sesac_3rd.config;

import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class ShedLockRedisConfig {
//    여러 서버에서 동일한 @Scheduled 작업이 동시에 실행되는 것을 방지
    @Bean
    public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockProvider(redisConnectionFactory);
    }
}
