package com.eighteenthstreet.deliveryservice.application.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.deliveryservice.application.dto.DeliveryRouteDto;

@FeignClient(name = "delivery-route-service")
public interface DeliveryRouteClient {
	@DeleteMapping("/api/v1/delivery-routes/by-delivery/{deliveryId}")
	ResponseEntity<Map<String, String>> deleteDeliveryRouteByDeliveryId(@PathVariable("deliveryId") UUID deliveryId);

	@GetMapping("/api/v1/delivery-routes/by-delivery/{deliveryId}")
	ResponseEntity<List<DeliveryRouteDto>> getDeliveryRoutesByDeliveryId(@PathVariable("deliveryId") UUID deliveryId);
}
