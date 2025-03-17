package com.eighteenthstreet.slack_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"auth", "base", "exception", "com.eighteenthstreet.slack_service"})
public class SlackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackServiceApplication.class, args);
	}

}
