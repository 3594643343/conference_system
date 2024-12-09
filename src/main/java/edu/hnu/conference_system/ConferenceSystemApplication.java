package edu.hnu.conference_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;


@MapperScan("edu.hnu.conference_system.mapper")
@SpringBootApplication
public class ConferenceSystemApplication {

	public static void main(String[] args) throws Exception {
		String IP = InetAddress.getLocalHost().getHostAddress();
		System.out.println("ip为:"+IP);
		SpringApplication.run(ConferenceSystemApplication.class, args);
	}

}
