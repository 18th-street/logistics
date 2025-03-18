package com.eigtheenthstreet.order_service.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
	CREATED("주문 생성"),        // 주문이 생성됨
	SHIPPED("배송 중"),          // 상품이 배송 시작됨
	DELIVERED("배송 완료"),      // 상품이 고객에게 전달됨
	DELIVERY_FAILED("배송 생성 실패"), // 배송 생성 실패
	CANCELLED("주문 취소"),      // 주문 삭제 또는 주문 취소 시
	FAILED("주문 실패");         // 재고 부족 등 이유로 실패

	private final String description;

	public static boolean isUpdateOrderStatusNotAllowed(OrderStatus orderStatus) {
		return orderStatus != CREATED;
	}

	public static boolean isDeleteOrderStatusNotAllowed(OrderStatus orderStatus) {
		return orderStatus != CREATED;
	}

	public static boolean isCancelOrderStatusNotAllowed(OrderStatus orderStatus) {
		return orderStatus == DELIVERY_FAILED || orderStatus == FAILED;
	}
}
