package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCreatedCompleteMessage(
	UUID orderId,
	UUID deliveryId
) {
}
