package com.eighteenthstreet.deliveryservice.applicatoin.dto;

import java.util.UUID;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetDeliveryResponse {
	private UUID deliveryId;
	private UUID orderId;
	private UUID startHubId;
	private UUID endHubId;
	private DeliveryStatus status;

	public static GetDeliveryResponse fromEntity(Delivery delivery) {
		return GetDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.orderId(delivery.getOrderId())
			.startHubId(delivery.getStartHubId())
			.endHubId(delivery.getEndHubId())
			.status(delivery.getStatus())
			.build();
	}
}
