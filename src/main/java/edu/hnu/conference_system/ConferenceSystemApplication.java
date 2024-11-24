package edu.hnu.conference_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import xunfei_api.IatModelZhMain;

import static xunfei_api.IatModelZhMain.*;

@MapperScan("edu.hnu.conference_system.mapper")
@SpringBootApplication


public class ConferenceSystemApplication {
	private static IatModelZhMain voiceToText;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConferenceSystemApplication.class, args);
		// 调用语音转写功能
		voiceToText.VoiceToText(args);
	}

}
