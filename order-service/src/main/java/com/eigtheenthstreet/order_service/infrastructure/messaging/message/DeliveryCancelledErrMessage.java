package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCancelledErrMessage(
	UUID orderId,
	String failureReason
) {
}
