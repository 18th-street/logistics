package com.eighteenthstreet.deliveryservice.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryCancelledErrMessage(
	UUID orderId,
	String failureReason
) {
}