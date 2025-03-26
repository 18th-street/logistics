package com.eighteenthstreet.deliveryservice.infrastructure.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.deliveryservice.application.dto.DeliveryAgentDto;

@FeignClient(name = "delivery-agent-service")
public interface DeliveryAgentClient {

	@DeleteMapping("/api/v1/delivery-agents/by-delivery/{deliveryId}")
	ResponseEntity<Map<String, String>> deleteDeliveryAgentByDeliveryId(@PathVariable("deliveryId") UUID deliveryId);

	@GetMapping("/api/v1/delivery-agents/by-delivery/{deliveryId}")
	ResponseEntity<List<DeliveryAgentDto>> getDeliveryAgentsByDeliveryId(@PathVariable("deliveryId") UUID deliveryId);
}