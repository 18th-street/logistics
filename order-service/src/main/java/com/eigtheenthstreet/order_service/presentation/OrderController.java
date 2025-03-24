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
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.presentation.request.SearchCondition;
import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

import auth.CheckRole;
import auth.JwtUtil;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
	private final JwtUtil jwtUtil;
	private final OrderService orderService;

	@Operation(summary = "주문 등록", description = "모든 사용자는 주문 등록을 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB, Role.COMPANY, Role.DELIVERY})
	@PostMapping()
	public ResponseEntity<CreateOrderResponse> createOrder(
		@RequestBody CreateOrderRequest createOrderRequest,
		@RequestHeader("Authorization") String token
	) {
		UUID userId = jwtUtil.getUserIdFromToken(token);
		CreateOrderResponse response = orderService.registerOrder(createOrderRequest, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "주문 수정", description = "마스터 관리자, 허브 관리자만 주문 수정을 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@PatchMapping("/{orderId}")
	public ResponseEntity<UpdateOrderResponse> updateOrder(
		@PathVariable UUID orderId,
		@RequestBody UpdateOrderRequest request
	) {
		UpdateOrderResponse response = orderService.updateOrder(request, orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "주문 삭제", description = "마스터 관리자, 허브 관리자만 주문 삭제를 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(
		@PathVariable UUID orderId
	) {
		orderService.deleteOrder(orderId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "주문 취소", description = "마스터 관리자, 허브 관리자만 주문 취소를 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@DeleteMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(
		@PathVariable UUID orderId
	) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "주문 조회", description = "모든 사용자는 주문 조회를 할 수 있습니다.")
	@GetMapping("/{orderId}")
	public ResponseEntity<SelectOrderResponse> getOrder(@PathVariable UUID orderId) {
		SelectOrderResponse response = orderService.getOrder(orderId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "주문 검색", description = "모든 사용자는 주문 검색을 할 수 있습니다.")
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
}
