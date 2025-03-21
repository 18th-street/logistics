package com.eighteenthstreet.hub_route_service.presentation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_route_service.application.HubRouteService;
import com.eighteenthstreet.hub_route_service.application.dto.CreateHubRouteRequest;
import com.eighteenthstreet.hub_route_service.application.dto.GetHubRouteResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub/routes")
@RequiredArgsConstructor
public class HubRouteController {

	private final HubRouteService hubRouteService;

	@GetMapping()
	public ResponseEntity<Map<String, List<GetHubRouteResponse>>> findOptimalRoute(@RequestParam UUID departureHubId,
		@RequestParam UUID arrivalHubId) {

		List<GetHubRouteResponse> optimalRoutes = hubRouteService.findOptimalRoute(departureHubId, arrivalHubId);

		// 결과를 "routes" 키를 가지는 JSON 객체로 변환
		Map<String, List<GetHubRouteResponse>> response = Map.of("routes", optimalRoutes);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/add")
	public ResponseEntity<GetHubRouteResponse> addHubRoute(@RequestBody CreateHubRouteRequest request) {
		GetHubRouteResponse response = hubRouteService.addHubRoute(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
