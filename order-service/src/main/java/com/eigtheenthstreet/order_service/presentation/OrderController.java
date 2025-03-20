package com.eigtheenthstreet.order_service.presentation;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eigtheenthstreet.order_service.application.OrderService;
import com.eigtheenthstreet.order_service.application.dto.CreateOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.SelectOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.UpdateOrderResponse;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderRoleDeniedException;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.presentation.request.SearchCondition;
import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

import auth.JwtUtil;
import auth.Role;
import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
	private final JwtUtil jwtUtil;
	private final OrderService orderService;

	@PostMapping()
	public ResponseEntity<CreateOrderResponse> createOrder(
		@RequestBody CreateOrderRequest createOrderRequest,
		@RequestHeader("Authorization") String token
	) {
		UUID userId = jwtUtil.getUserIdFromToken(token);
		CreateOrderResponse response = orderService.registerOrder(createOrderRequest, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<UpdateOrderResponse> updateOrder(
		@PathVariable UUID orderId,
		@RequestBody UpdateOrderRequest request,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidUpdateRole(role);

		UpdateOrderResponse response = orderService.updateOrder(request, orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(
		@PathVariable UUID orderId,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidDeleteRole(role);

		orderService.deleteOrder(orderId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(
		@PathVariable UUID orderId,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidCancelRole(role);

		orderService.cancelOrder(orderId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<SelectOrderResponse> getOrder(@PathVariable UUID orderId) {
		SelectOrderResponse response = orderService.getOrder(orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping()
	public ResponseEntity<PagedModel<SelectOrderResponse>> getAllOrders(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(name = "q", required = false) String query
	) {
		SearchCondition searchCondition = SearchCondition.of(
			String.valueOf(page),
			String.valueOf(size),
			sort,
			query
		);

		Pageable pageable = PageRequest.of(
			searchCondition.getPage(),
			searchCondition.getSize(),
			searchCondition.getSort().getSort()
		);

		PagedModel<SelectOrderResponse> response = orderService.getAllOrders(query, pageable);
		return ResponseEntity.ok(response);
	}

	private void hasValidUpdateRole(Role role) {
		if (role == null || !role.equals(Role.MASTER) && !role.equals(Role.HUB)) {
			throw new CustomOrderRoleDeniedException(ErrorCode.ORDER_UPDATE_ROLE_DENIED);
		}
	}

	private void hasValidDeleteRole(Role role) {
		if (role == null || !role.equals(Role.MASTER) && !role.equals(Role.HUB)) {
			throw new CustomOrderRoleDeniedException(ErrorCode.ORDER_DELETE_ROLE_DENIED);
		}
	}

	private void hasValidCancelRole(Role role) {
		if (role == null || !role.equals(Role.MASTER) && !role.equals(Role.HUB)) {
			throw new CustomOrderRoleDeniedException(ErrorCode.ORDER_CANCEL_ROLE_DENIED);
		}
	}
}
