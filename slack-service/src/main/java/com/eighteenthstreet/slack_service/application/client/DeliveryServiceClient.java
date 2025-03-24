package com.eighteenthstreet.slack_service.application.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.slack_service.application.dto.DeliveryDetailsResponse;

@FeignClient(name = "delivery-service", url = "http://localhost:19100")
public interface DeliveryServiceClient {
	@GetMapping("/api/vi/deliveries/{id}")
	DeliveryDetailsResponse getDelivery(@PathVariable("id") UUID uuid);
}
