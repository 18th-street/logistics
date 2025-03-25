package com.eighteenthstreet.hub_route_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"exception", "base", "config", "com.eighteenthstreet.hub_service",
	"com.eighteenthstreet.hub_route_service"})
@EnableDiscoveryClient
public class HubRouteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubRouteServiceApplication.class, args);
	}

}
