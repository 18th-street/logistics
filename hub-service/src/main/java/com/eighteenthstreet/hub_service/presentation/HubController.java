package com.eighteenthstreet.hub_service.presentation;

import java.util.List;
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

import auth.CheckRole;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "허브 컨트롤러")
@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
@Tag(name = "Hub API", description = "허브 관련 API")
public class HubController {

	private final HubService hubService;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@CheckRole({Role.MASTER})
	@Operation(summary = "허브 생성", description = "허브를 생성합니다.")
	@PostMapping()
	public ResponseEntity<CreateHubResponse> createHub(@RequestBody CreateHubRequest request) {
		CreateHubResponse response = hubService.createHub(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "허브 목록 조회", description = "페이지네이션 및 키워드를 기반으로 허브 목록을 조회합니다.")
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

	@Operation(summary = "허브 단건 조회", description = "허브 ID를 기반으로 단건 정보를 조회합니다.")
	@GetMapping("/{hubId}")
	public ResponseEntity<GetHubResponse> getHub(@PathVariable UUID hubId) {
		GetHubResponse response = hubService.getHub(hubId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "허브 수정", description = "허브 ID와 요청 정보를 기반으로 허브를 수정합니다.")
	@CheckRole({Role.MASTER})
	@PutMapping("/{hubId}")
	public ResponseEntity<UpdateHubResponse> updateHub(@PathVariable UUID hubId,
		@RequestBody UpdateHubRequest request) {
		UpdateHubResponse response = hubService.updateHub(hubId, request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@CheckRole({Role.MASTER})
	@Operation(summary = "허브 삭제", description = "허브 ID를 기반으로 허브를 삭제합니다.")
	@DeleteMapping("/{hubId}")
	public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(hubId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "허브 존재 여부 확인", description = "허브 ID를 기반으로 허브 존재 여부를 확인합니다.")
	@GetMapping("/{hubId}/exists")
	public boolean existsById(@PathVariable("hubId") UUID hubId) {
		return hubService.existsById(hubId);
	}

	@Operation(summary = "허브 ID 리스트로 조회", description = "다수의 허브 ID를 기반으로 허브 정보를 조회합니다.")
	@PostMapping("/ids")
	public ResponseEntity<List<GetHubResponse>> getHubsByIds(@RequestBody List<UUID> hubIds) {
		List<GetHubResponse> response = hubService.getHubsById(hubIds);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	;
}
