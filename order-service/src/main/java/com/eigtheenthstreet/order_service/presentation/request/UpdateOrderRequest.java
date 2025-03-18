package com.eigtheenthstreet.order_service.presentation.request;

import java.util.List;
import java.util.UUID;

public record UpdateOrderRequest(List<UpdateOrderItemRequest> orderItems) {
	public record UpdateOrderItemRequest(
		UUID productId,
		Integer productQuantity
	) {
	}
}
