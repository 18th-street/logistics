package com.eighteenthstreet.slack_service;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableFeignClients
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication(scanBasePackages = {"auth", "base", "exception", "com.eighteenthstreet.slack_service"})
public class SlackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackServiceApplication.class, args);
	}

}
