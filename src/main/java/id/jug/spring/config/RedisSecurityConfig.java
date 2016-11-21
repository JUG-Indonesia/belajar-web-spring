package id.jug.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by galih.lasahido@gmail.com
 */
@Configuration
@EnableRedisHttpSession(redisNamespace="belajarspring")
public class RedisSecurityConfig {
	
	@Bean
    public JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory(); 
    }
}
