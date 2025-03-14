package com.eighteenthstreet.deliveryservice.presentation;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryservice.applicatoin.DeliveryService;
import com.eighteenthstreet.deliveryservice.applicatoin.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	//TODO: 전체적으로 gateWay 가 생기면 jwt 를 받는 헤더를 추가 할 예정, 예외처리도 할 예정

	@PostMapping()
	public ResponseEntity<CreateDeliveryResponse> createDelivery(
		@RequestBody CreateDeliveryRequest request) {
		CreateDeliveryResponse response = deliveryService.createDelivery(request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping()
	public ResponseEntity<Map<String, String>> updateDeliveryStatus(@RequestBody UpdateStatusDeliveryRequest request) {
		deliveryService.updateDeliveryStatus(request);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달 상태가 변경 됐습니다."));
	}

}
