package com.eigtheenthstreet.order_service.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eigtheenthstreet.order_service.application.OrderService;
import com.eigtheenthstreet.order_service.application.dto.CreateOrderResponse;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
	private final OrderService orderService;

	@PostMapping()
	public ResponseEntity<CreateOrderResponse> createOrder(
		@RequestBody CreateOrderRequest createOrderRequest,
		Long userId
	) {
		CreateOrderResponse response = orderService.registerOrder(createOrderRequest, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
