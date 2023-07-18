package baby.lignin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.data.host}")
    private String host;

    @Value("${spring.redis.data.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() { // redis 서버와 연결할 수 있는 객체 생성
        return new LettuceConnectionFactory( // lettuce 객체를 생성하고
                new RedisStandaloneConfiguration(host, port) // RedisStandaloneConfiguration 객체는 Redis 호스트와 포트를 설정하는 역할
        );
    }
}