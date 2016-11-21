package id.jug.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:./resources/config/application.properties")
public class BelajarWebSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(BelajarWebSpringApplication.class, args);
	}
}
