package com.eighteenthstreet.deliveryrouteservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eighteenthstreet.deliveryrouteservice.infrastructure.client.dto.GetHubRoutesResponse;

@FeignClient(name = "hub-route-service")
public interface HubRouteClient {
	@GetMapping("/api/v1/hub/routes")
	GetHubRoutesResponse getHubRoutes(@RequestParam("departureHubId") UUID departureHubId,
		@RequestParam("arrivalHubId") UUID arrivalHubId);
}
