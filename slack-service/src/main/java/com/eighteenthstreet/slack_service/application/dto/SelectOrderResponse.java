package com.eighteenthstreet.slack_service.application.dto;

import java.util.List;
import java.util.UUID;

public record SelectOrderResponse(
	UUID orderId,
	UUID ordererId,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	String requestDetails,
	Integer orderTotalQuantity,
	Integer orderTotalAmount,
	List<SelectOrderItemResponse> orderItems,
	UUID deliveryId,
	OrderStatus orderStatus
) {
	public record SelectOrderItemResponse(
		UUID productId,
		String productName,
		Integer productQuantity,
		Integer productPrice,
		Integer productTotalPrice
	) {
	}

	public enum OrderStatus {
		CREATED, SHIPPED, DELIVERED, DELIVERY_FAILED, CANCELLED, FAILED;
	}
}
