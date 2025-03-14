package com.eighteenthstreet.deliveryservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryservice.applicatoin.DeliveryService;
import com.eighteenthstreet.deliveryservice.applicatoin.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	//TODO: 전체적으로 gateWay 가 생기면 jwt 를 받는 헤더를 추가 할 예정, 예외처리도 할 예정

	@PostMapping()
	public ResponseEntity<CreateDeliveryResponse> createDelivery(
		@RequestBody CreateDeliveryRequest createDeliveryRequest) {
		CreateDeliveryResponse response = deliveryService.createDelivery(createDeliveryRequest);

		return ResponseEntity.ok(response);
	}

}
