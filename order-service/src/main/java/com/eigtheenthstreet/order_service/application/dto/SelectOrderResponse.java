package com.eigtheenthstreet.order_service.application.dto;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;

public record SelectOrderResponse(
	UUID orderId,
	//UUID ordererId,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	Integer orderTotalQuantity,
	Integer orderTotalAmount,
	List<SelectOrderItemRequest> orderItems,
	//UUID deliveryId,
	OrderStatus orderStatus
) {
	public static SelectOrderResponse from(
		Order order,
		List<SelectOrderItemRequest> orderItem
	) {
		return new SelectOrderResponse(
			order.getId(),
			order.getSupplierCompanyId(),
			order.getConsumerCompanyId(),
			order.getQuantity(),
			order.getTotalAmount(),
			orderItem,
			order.getOrderStatus()
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
