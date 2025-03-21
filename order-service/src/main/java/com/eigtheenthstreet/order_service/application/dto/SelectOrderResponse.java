package com.eigtheenthstreet.order_service.application.dto;

import java.util.List;
import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.GetBulkProductResponse;
import com.eigtheenthstreet.order_service.util.DateTimeUtil;

public record SelectOrderResponse(
	UUID orderId,
	UUID ordererId,
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	Integer orderTotalQuantity,
	Integer orderTotalAmount,
	String deliveryLimitedAt,
	List<SelectOrderItemResponse> orderItems,
	UUID deliveryId,
	OrderStatus orderStatus
) {
	public static SelectOrderResponse from(
		Order order,
		List<SelectOrderItemResponse> orderItem
	) {
		return new SelectOrderResponse(
			order.getId(),
			order.getOrdererId(),
			order.getSupplierCompanyId(),
			order.getConsumerCompanyId(),
			order.getQuantity(),
			order.getTotalAmount(),
			DateTimeUtil.formatDateTime(order.getDeliveryLimitedAt()),
			orderItem,
			order.getDeliveryId(),
			order.getOrderStatus()
		);
	}

	public record SelectOrderItemResponse(
		UUID productId,
		String productName,
		Integer productQuantity,
		Integer productPrice,
		Integer productTotalPrice
	) {
		public static SelectOrderItemResponse from(GetBulkProductResponse productResponse, OrderItem orderItem) {
			return new SelectOrderItemResponse(
				productResponse.productId(),
				productResponse.productName(),
				orderItem.getQuantity(),
				productResponse.productPrice(),
				orderItem.getTotalPrice()
			);
		}
	}
}
