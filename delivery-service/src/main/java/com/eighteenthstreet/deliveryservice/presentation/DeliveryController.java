package com.eighteenthstreet.deliveryservice.presentation;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryservice.application.DeliveryService;
import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	@PostMapping()
	@Operation(summary = "새 배달 생성", description = "제공된 정보로 새 배달을 생성합니다.")
	public ResponseEntity<CreateDeliveryResponse> createDelivery(
		@RequestBody @Parameter(description = "배달 정보를 포함한 요청 본문") CreateDeliveryRequest request) {
		CreateDeliveryResponse response = deliveryService.createDelivery(request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping()
	@Operation(summary = "배달 상태 수정", description = "기존 배달의 상태를 수정합니다.")
	public ResponseEntity<Map<String, String>> updateDeliveryStatus(
		@RequestBody @Parameter(description = "수정된 배달 상태를 포함한 요청 본문") UpdateStatusDeliveryRequest request) {
		deliveryService.updateDeliveryStatus(request);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달 상태가 변경 됐습니다."));
	}

	@GetMapping("/{id}")
	@Operation(summary = "배달 정보 조회", description = "UUID로 특정 배달의 세부 정보를 조회합니다.")
	public ResponseEntity<DeliveryDetailsResponse> getDelivery(
		@PathVariable("id") @Parameter(description = "배달의 UUID") UUID uuid) {
		DeliveryDetailsResponse response = deliveryService.getDeliveryDetails(uuid);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "배달 삭제", description = "UUID로 특정 배달을 삭제합니다.")
	public ResponseEntity<Map<String, String>> deleteDelivery(
		@PathVariable("id") @Parameter(description = "삭제할 배달의 UUID") UUID uuid) {
		deliveryService.deleteDelivery(uuid);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달이 취소되었습니다."));
	}
}