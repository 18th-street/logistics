package com.eigtheenthstreet.order_service.application.dto;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;

public record UpdateOrderResponse(
	UUID orderId,
	//UUID ordererId,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	Integer orderTotalQuantity,
	Integer orderTotalAmount,
	List<UpdateOrderItemResponse> orderItems,
	//UUID deliveryId,
	OrderStatus orderStatus
) {
	public static UpdateOrderResponse of(Order order, List<UpdateOrderItemResponse> orderItems) {
		return new UpdateOrderResponse(
			order.getId(),
			order.getSupplierCompanyId(),
			order.getConsumerCompanyId(),
			order.getQuantity(),
			order.getTotalAmount(),
			orderItems,
			order.getOrderStatus()
		);
	}

	public record UpdateOrderItemResponse(
		UUID productId,
		Integer productQuantity,
		Integer productTotalPrice
	) {
		public static UpdateOrderItemResponse from(OrderItem orderItem) {
			return new UpdateOrderItemResponse(
				orderItem.getProductId(),
				orderItem.getQuantity(),
				orderItem.getTotalPrice()
			);
		}
	}
}
