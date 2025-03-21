package com.eighteenthstreet.deliveryrouteservice.domain.event;

import java.util.UUID;

public record OrderDeliveryCompleteMessage(
	UUID orderId,
	UUID deliveryId
) {
}