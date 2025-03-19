package com.eighteenthstreet.hub_route_service.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_route_service.application.HubRouteService;
import com.eighteenthstreet.hub_route_service.application.dto.GetHubRoutesResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub/routes")
@RequiredArgsConstructor
public class HubRouteController {

	private final HubRouteService hubRouteService;

	// @PostMapping("/generate")
	// public ResponseEntity<String> generateRoutes() {
	// 	hubRouteService.generateHubRoutes();
	// 	return ResponseEntity.ok("허브 간 이동 경로 생성 완료");
	// }

	@GetMapping()
	public ResponseEntity<GetHubRoutesResponse> getHubRoutes(@RequestParam UUID departureHubId,
		@RequestParam UUID arrivalHubId) {
		GetHubRoutesResponse hubRoutes = hubRouteService.getHubRoutes(departureHubId, arrivalHubId);

		return ResponseEntity.status(HttpStatus.OK).body(hubRoutes);
	}
}
