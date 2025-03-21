package com.eighteenthstreet.slack_service.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eighteenthstreet.slack_service.application.dto.GetHubResponse;

@FeignClient(name = "hub-service", url = "http://localhost:19095")
public interface HubServiceClient {
	@PostMapping("/api/v1/hub")
	List<GetHubResponse> getHubsByIds(@RequestBody List<UUID> hubIds);

	@GetMapping("/api/v1/hub/{hubId}")
	GetHubResponse getHub(@PathVariable("hubId") UUID hubId);
}
