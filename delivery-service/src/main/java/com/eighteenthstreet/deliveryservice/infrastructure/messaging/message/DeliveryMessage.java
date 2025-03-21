package com.eighteenthstreet.deliveryservice.infrastructure.messaging.message;

import java.util.UUID;

public record DeliveryMessage(
	UUID orderId,
	UUID startHubId,
	UUID endHubId,
	UUID ordererId,
	String destinationAddress
) {
}
