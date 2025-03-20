package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;

public record SlackMessage(UUID orderId) {
	public static SlackMessage from(Order order) {
		return new SlackMessage(order.getId());
	}
}
