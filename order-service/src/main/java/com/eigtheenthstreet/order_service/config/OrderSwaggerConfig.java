package com.eigtheenthstreet.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OrderSwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo());
	}

	private io.swagger.v3.oas.models.info.Info apiInfo() {
		return new Info()
			.title("Order Service Swagger API")
			.description("order-service REST API 입니다.")
			.version("1.0");
	}
}
