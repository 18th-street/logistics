package com.eighteenthstreet.deliveryservice.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCancelledEvent(
	UUID orderId,
	UUID deliveryId
) {
}
