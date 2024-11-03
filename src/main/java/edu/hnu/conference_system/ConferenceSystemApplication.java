package edu.hnu.conference_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ConferenceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceSystemApplication.class, args);
	}

}
