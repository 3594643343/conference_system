package edu.hnu.conference_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("edu.hnu.conference_system.service.mapper")
@SpringBootApplication
public class ConferenceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceSystemApplication.class, args);
	}

}
