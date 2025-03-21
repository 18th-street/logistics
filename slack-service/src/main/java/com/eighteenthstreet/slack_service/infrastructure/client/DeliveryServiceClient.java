package com.eighteenthstreet.slack_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.slack_service.application.dto.GetDeliveryResponse;

@FeignClient(name = "delivery-service")
public interface DeliveryServiceClient {
	@GetMapping("/api/vi/deliveries/{id}")
	GetDeliveryResponse getDelivery(@PathVariable("id") UUID uuid);
}
