package com.ims.msnotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsNotificationServiceApplication {

//	@Bean
//	public SimpleMailMessage templateSimpleMessage() {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setText(
//				"This is the test email template for your email:\n%s\n");
//		return message;
//	}
	public static void main(String[] args) {
		SpringApplication.run(MsNotificationServiceApplication.class, args);
	}

}
