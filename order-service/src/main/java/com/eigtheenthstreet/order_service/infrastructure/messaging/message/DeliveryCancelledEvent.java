package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCancelledEvent(
	UUID orderId,
	UUID deliveryId
) {
	public static DeliveryCancelledEvent from(UUID orderId, UUID deliveryId) {
		return new DeliveryCancelledEvent(orderId, deliveryId);
	}
}
