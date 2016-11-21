package id.jug.spring.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by galih.lasahido@gmail.com
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "id.jug.spring.repositories")
@EntityScan(basePackages="id.jug.spring.domain")
public class DBConfig { 
}
