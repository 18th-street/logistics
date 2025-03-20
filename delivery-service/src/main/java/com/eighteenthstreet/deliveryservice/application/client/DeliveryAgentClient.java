package com.eighteenthstreet.deliveryservice.application.client;

import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-agent-service")
public interface DeliveryAgentClient {

	@DeleteMapping("/api/v1/delivery-agents/by-delivery/{deliveryId}")
	ResponseEntity<Map<String, String>> deleteDeliveryAgentByDeliveryId(@PathVariable("deliveryId") UUID deliveryId);
}