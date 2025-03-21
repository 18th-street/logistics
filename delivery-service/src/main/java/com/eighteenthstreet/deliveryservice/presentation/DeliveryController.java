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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	@PostMapping()
	public ResponseEntity<CreateDeliveryResponse> createDelivery(@RequestBody CreateDeliveryRequest request) {
		CreateDeliveryResponse response = deliveryService.createDelivery(request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping()
	public ResponseEntity<Map<String, String>> updateDeliveryStatus(@RequestBody UpdateStatusDeliveryRequest request) {
		deliveryService.updateDeliveryStatus(request);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달 상태가 변경 됐습니다."));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeliveryDetailsResponse> getDelivery(@PathVariable("id") UUID uuid) {
		DeliveryDetailsResponse response = deliveryService.getDeliveryDetails(uuid);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteDelivery(@PathVariable("id") UUID uuid) {
		deliveryService.deleteDelivery(uuid);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달이 취소되었습니다."));
	}

}
