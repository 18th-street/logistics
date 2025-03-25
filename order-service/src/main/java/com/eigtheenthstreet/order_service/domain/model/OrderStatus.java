package com.eigtheenthstreet.order_service.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
	CREATED("주문 생성"),                    // 주문이 생성됨
	SHIPPED("배송 중"),                     // 상품이 배송 시작됨
	DELIVERED("배송 완료"),                 // 상품이 고객에게 전달됨
	DELIVERY_FAILED("배송 생성 실패"),      // 배송 생성 실패

	CANCELLING("취소 진행 중"),                //주문 취소 프로세스 진행 중
	CANCELLED("주문 취소"),                   // 주문 취소 시
	DELIVERY_CANCEL_FAILED("배송 취소 실패"),  // 배송 취소 실패

	DELETED("주문 삭제"),                    // 주문 삭제 시
	FAILED("주문 실패");                   // 재고 부족

	private final String description;

	public static boolean isUpdateOrderStatusNotAllowed(OrderStatus orderStatus) {
		return isOrderFinalized(orderStatus);
	}

	public static boolean isDeleteOrderStatusNotAllowed(OrderStatus orderStatus) {
		return isOrderFinalized(orderStatus);
	}

	public static boolean isCancelOrderStatusNotAllowed(OrderStatus orderStatus) {
		return isOrderFinalized(orderStatus) || orderStatus == CANCELLING || orderStatus == CANCELLED;
	}

	private static boolean isOrderFinalized(OrderStatus orderStatus) {
		return orderStatus == SHIPPED || orderStatus == DELIVERED ||
			orderStatus == FAILED || orderStatus == DELIVERY_FAILED;
	}
}
