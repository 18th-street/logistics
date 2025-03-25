package com.eigtheenthstreet.order_service.infrastructure.messaging.message;

import java.util.UUID;

import com.eigtheenthstreet.order_service.domain.model.Order;

public record DeliveryCreatedEvent(
	UUID orderId,
	UUID startHubId,
	UUID endHubId,
	UUID ordererId,
	String destinationAddress
) {
	public static DeliveryCreatedEvent of(
		Order order,
		UUID startHubId,
		UUID endHubId
	) {
		return new DeliveryCreatedEvent(
			order.getId(),
			startHubId,
			endHubId,
			order.getOrdererId(),
			order.getDestinationAddress()
		);
	}
}
