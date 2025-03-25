package com.eighteenthstreet.hub_route_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("hubRouteAppConfig")
public class AppConfig {

	@Bean(name = "hubRouteRestTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
