package com.eighteenthstreet.deliveryagentservice.presentation;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryagentservice.application.DeliveryAgentService;
import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.application.dto.GetDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import com.eighteenthstreet.deliveryagentservice.presentation.request.UpdateDeliveryTypeRequest;

import auth.CheckRole;
import auth.JwtUtil;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/delivery-agents")
@RequiredArgsConstructor
public class DeliveryAgentController {
	private final DeliveryAgentService deliveryAgentService;
	private final JwtUtil jwtUtil;

	@CheckRole({Role.MASTER, Role.HUB, Role.COMPANY, Role.DELIVERY})
	@PostMapping
	@Operation(summary = "새 배달 담당자 생성", description = "제공된 정보로 새 배달 담당자를 생성합니다.")
	public ResponseEntity<CreateDeliveryAgentResponse> createDeliveryAgent(
		@RequestBody @Parameter(description = "배달 담당자 정보를 포함한 요청 본문") CreateDeliveryAgentRequest request,
		@RequestHeader("Authorization") @Parameter(description = "인증을 위한 JWT 토큰") String token) {

		UUID userId = jwtUtil.getUserIdFromToken(token);

		CreateDeliveryAgentResponse response = deliveryAgentService.createDeliveryAgent(request, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("{id}")
	@Operation(summary = "배달 담당자 조회", description = "UUID로 특정 배달 담당자의 세부 정보를 조회합니다.")
	public ResponseEntity<GetDeliveryAgentResponse> getDeliveryAgent(
		@PathVariable(name = "id") @Parameter(description = "배달 담당자의 UUID") UUID id) {
		GetDeliveryAgentResponse response = deliveryAgentService.getDeliveryAgent(id);

		return ResponseEntity.ok(response);
	}

	@PatchMapping()
	@Operation(summary = "배달 담당자 타입 수정", description = "기존 배달 담당자의 타입을 수정합니다.")
	public ResponseEntity<Map<String, String>> updateDeliveryAgentType(
		@RequestBody @Parameter(description = "수정된 배달 타입을 포함한 요청 본문") UpdateDeliveryTypeRequest request) {
		deliveryAgentService.updateDeliveryAgentType(request);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달담당자 타입이 변경되었습니다."));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "배달 담당자 삭제", description = "UUID로 특정 배달 담당자를 삭제합니다.")
	public ResponseEntity<Map<String, String>> deleteDeliveryAgent(
		@PathVariable("id") @Parameter(description = "삭제할 배달 담당자의 UUID") UUID id) {
		deliveryAgentService.deleteDeliveryAgent(id);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달담당자가 삭제되었습니다."));
	}
}