package todo.list_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ListServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListServiceApplication.class, args);
	}

}
