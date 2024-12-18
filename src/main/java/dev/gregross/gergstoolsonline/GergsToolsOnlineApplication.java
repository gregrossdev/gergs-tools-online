package dev.gregross.gergstoolsonline;

import dev.gregross.gergstoolsonline.tool.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GergsToolsOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(GergsToolsOnlineApplication.class, args);
	}

	@Bean
	public IdWorker idWorker(){
		return new IdWorker(1, 1);
	}

}
