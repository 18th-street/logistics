package com.eigtheenthstreet.order_service.application.dto;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;

public record SelectOrderResponse(
	UUID orderId,
	//UUID ordererId,
	OrderStatus orderStatus,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	List<SelectOrderItemRequest> orderItems
) {
	public static SelectOrderResponse from(
		Order order,
		List<SelectOrderItemRequest> orderItem
	) {
		return new SelectOrderResponse(
			order.getId(),
			order.getOrderStatus(),
			order.getSupplierCompanyId(),
			order.getConsumerCompanyId(),
			orderItem
		);
	}

	public record SelectOrderItemRequest(
		UUID productId,
		Integer productQuantity,
		Integer productTotalPrice
	) {
		public static SelectOrderItemRequest from(OrderItem orderItem) {
			return new SelectOrderItemRequest(
				orderItem.getProductId(),
				orderItem.getQuantity(),
				orderItem.getTotalPrice()
			);
		}
	}
}
