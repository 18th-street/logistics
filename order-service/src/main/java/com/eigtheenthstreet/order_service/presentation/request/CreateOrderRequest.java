package com.eigtheenthstreet.order_service.presentation.request;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
	UUID supplierCompanyId,
	UUID consumerCompanyId,
	String requestDetails,
	String deliveryAddress,
	//UUID slackId,
	List<OrderItemReqeust> orderItems
) {
	public record OrderItemReqeust(
		UUID productId,
		Integer productQuantity
	) {
	}
}
