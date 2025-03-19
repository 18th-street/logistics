package com.eighteenthstreet.deliveryrouteservice.application.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-route-service")
public interface HubRouteClient {
	@GetMapping("/api/v1/hub/routes")
	GetHubRoutesResponse getHubRoutes(@RequestParam("departureHubId") UUID departureHubId,
		@RequestParam("arrivalHubId") UUID arrivalHubId);
}
