package com.eighteenthstreet.hub_service.presentation;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_service.application.HubService;
import com.eighteenthstreet.hub_service.application.dto.CreateHubResponse;
import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.application.dto.UpdateHubResponse;
import com.eighteenthstreet.hub_service.presentation.request.CreateHubRequest;
import com.eighteenthstreet.hub_service.presentation.request.UpdateHubRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "허브 컨트롤러")
@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@PostMapping()
	public ResponseEntity<CreateHubResponse> createHub(@RequestBody CreateHubRequest request) {
		CreateHubResponse response = hubService.createHub(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping()
	public ResponseEntity<PagedModel<GetHubResponse>> searchHubs(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "keyword", required = false) String keyword
	) {

		PageRequest pageable = PageRequest.of(page, size);

		log.info(pageable.toString());

		PagedModel<GetHubResponse> response = hubService.searchHubs(pageable, keyword);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{hubId}")
	public ResponseEntity<GetHubResponse> getHub(@PathVariable UUID hubId) {
		GetHubResponse response = hubService.getHub(hubId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/{hubId}")
	public ResponseEntity<UpdateHubResponse> updateHub(@PathVariable UUID hubId,
		@RequestBody UpdateHubRequest request) {
		UpdateHubResponse response = hubService.updateHub(hubId, request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{hubId}")
	public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(hubId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{hubId}/exists")
	public boolean existsById(@PathVariable("hubId") UUID hubId) {
		return hubService.existsById(hubId);
	}
}
