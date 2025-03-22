package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCancelledCompleteMessage(
	UUID orderId,
	UUID deliveryId
) {
}
