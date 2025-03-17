package com.eigtheenthstreet.order_service.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eigtheenthstreet.order_service.application.OrderService;
import com.eigtheenthstreet.order_service.application.dto.CreateOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.SelectOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.UpdateOrderResponse;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
	private final OrderService orderService;

	// todo. JWTUtils 사용해서 권한 체크

	@PostMapping()
	public ResponseEntity<CreateOrderResponse> createOrder(
		@RequestBody CreateOrderRequest createOrderRequest,
		Long userId
	) {
		CreateOrderResponse response = orderService.registerOrder(createOrderRequest, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<UpdateOrderResponse> updateOrder(
		@PathVariable UUID orderId,
		@RequestBody UpdateOrderRequest request
	) {
		UpdateOrderResponse response = orderService.updateOrder(request, orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId, Long userId) {
		orderService.deleteOrder(orderId, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId, Long userId) {
		orderService.cancelOrder(orderId, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<SelectOrderResponse> getOrder(@PathVariable UUID orderId) {
		SelectOrderResponse response = orderService.getOrder(orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
