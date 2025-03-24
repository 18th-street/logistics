package com.eighteenthstreet.user_service.infrastructure.config;

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
			.title("User Service")
			.version("1.0")
			.description("This is a user service");
		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
