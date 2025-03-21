package com.eighteenthstreet.slack_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.slack_service.application.dto.SelectOrderResponse;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
	@GetMapping("/api/v1/orders/{orderId}")
	SelectOrderResponse getOrder(@PathVariable UUID orderId);
}
