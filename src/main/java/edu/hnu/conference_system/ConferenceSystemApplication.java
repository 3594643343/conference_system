package edu.hnu.conference_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import xunfei_api.IatModelZhMain;

import java.net.InetAddress;

import static xunfei_api.IatModelZhMain.*;

@MapperScan("edu.hnu.conference_system.mapper")
@SpringBootApplication
public class ConferenceSystemApplication {
	//private static IatModelZhMain voiceToText;

	public static void main(String[] args) throws Exception {
		String IP = InetAddress.getLocalHost().getHostAddress();
		System.out.println("ip为:"+IP);
		SpringApplication.run(ConferenceSystemApplication.class, args);
	}

}
