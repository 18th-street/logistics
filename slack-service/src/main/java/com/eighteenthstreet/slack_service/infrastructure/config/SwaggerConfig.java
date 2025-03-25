package com.eighteenthstreet.slack_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		Info info = new Info()
			.title("Slack Service")
			.version("1.0")
			.description("This is a slack service");
		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
