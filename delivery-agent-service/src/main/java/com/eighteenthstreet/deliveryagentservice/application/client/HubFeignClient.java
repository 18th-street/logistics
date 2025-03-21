package com.eighteenthstreet.deliveryagentservice.application.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubFeignClient {
	@GetMapping("/api/v1/hub/{hubId}/exists")
	boolean existsById(@PathVariable("hubId") UUID hubId);
}
