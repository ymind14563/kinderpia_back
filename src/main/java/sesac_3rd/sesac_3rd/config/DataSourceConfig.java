package sesac_3rd.sesac_3rd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String primaryUrl;

    @Value("${spring.datasource.primary.username}")
    private String primaryUsername;

    @Value("${spring.datasource.primary.password}")
    private String primaryPassword;

    @Value("${spring.datasource.primary.driver-class-name}")
    private String primaryDriver;

    @Value("${spring.datasource.replica.url}")
    private String replicaUrl;

    @Value("${spring.datasource.replica.username}")
    private String replicaUsername;

    @Value("${spring.datasource.replica.password}")
    private String replicaPassword;

    @Value("${spring.datasource.replica.driver-class-name}")
    private String replicaDriver;

    @Bean
    @Primary
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("primary", primaryDataSource());
        dataSourceMap.put("replica", replicaDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        // replica 장애 시 기본값 primary 사용하도록 함
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());
        // ReplicationRoutingDataSource 를 제대로 초기화하도록 하기 위해 추가 (공식 문서 참고함, Spring DataSource)
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    @Bean
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .url(primaryUrl)
                .username(primaryUsername)
                .password(primaryPassword)
                .driverClassName(primaryDriver)
                .build();
    }

    @Bean
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
                .url(replicaUrl)
                .username(replicaUsername)
                .password(replicaPassword)
                .driverClassName(replicaDriver)
                .build();
    }
}
