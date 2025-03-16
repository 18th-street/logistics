package com.eigtheenthstreet.order_service.application.dto;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;

public record SelectOrderResponse(
	UUID orderId,
	//UUID ordererId,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	List<SelectOrderRequest> orderItems
) {
	public static SelectOrderResponse from(
		Order order,
		List<SelectOrderRequest> orderItem
	) {
		return new SelectOrderResponse(
			order.getId(),
			order.getSupplierCompanyId(),
			order.getConsumerCompanyId(),
			orderItem
		);
	}

	public record SelectOrderRequest(
		UUID productId,
		Integer productQuantity,
		Integer productTotalPrice
	) {
		public static SelectOrderRequest from(OrderItem orderItem) {
			return new SelectOrderRequest(
				orderItem.getProductId(),
				orderItem.getQuantity(),
				orderItem.getTotalPrice()
			);
		}
	}
}
