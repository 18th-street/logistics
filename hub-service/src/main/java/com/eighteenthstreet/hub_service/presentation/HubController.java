package com.eighteenthstreet.hub_service.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_service.application.HubService;
import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.domain.model.Hub;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/{hubId}")
	public ResponseEntity<GetHubResponse> getHub(@PathVariable UUID hubId) {
		GetHubResponse response = hubService.getHub(hubId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
