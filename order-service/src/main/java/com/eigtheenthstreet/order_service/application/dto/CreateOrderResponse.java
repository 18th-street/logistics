package com.eigtheenthstreet.order_service.application.dto;

import java.util.UUID;

public record CreateOrderResponse(UUID orderId) {
	public static CreateOrderResponse from(UUID orderId) {
		return new CreateOrderResponse(orderId);
	}
}
