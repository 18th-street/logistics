package com.eighteenthstreet.deliveryrouteservice.presentation;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryrouteservice.application.DeliveryRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-routes")
public class DeliveryRouteFeignController {

	private final DeliveryRouteService deliveryRouteService;

	@DeleteMapping("/by-delivery/{deliveryId}")
	public ResponseEntity<Map<String, String>> deleteByDeliveryId(@PathVariable("deliveryId") UUID deliveryId) {
		deliveryRouteService.deleteDeliveryRouteByDeliveryId(deliveryId);
		return ResponseEntity.ok(Collections.singletonMap("message", "배달담당자가 삭제되었습니다."));
	}
}
