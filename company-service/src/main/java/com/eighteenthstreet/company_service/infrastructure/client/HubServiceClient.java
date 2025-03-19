package com.eighteenthstreet.company_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.company_service.application.dto.GetHubResponse;

@FeignClient(name = "hub-service", url = "http://localhost:19095")
public interface HubServiceClient {
	@GetMapping("/api/v1/hub/{hubId}")
	GetHubResponse getHub(@PathVariable UUID hubId);
}
