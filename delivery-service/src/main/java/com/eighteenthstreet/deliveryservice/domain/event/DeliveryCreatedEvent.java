package com.eighteenthstreet.deliveryservice.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryCreatedEvent {
	private UUID startHubId;
	private UUID endHubId;
	private UUID deliveryId;
	private UUID orderId;
}
