package com.eighteenthstreet.deliveryservice.presentation.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateDeliveryRequest {
	private UUID orderId;
	private UUID startHubId;
	private UUID endHubId;
	private UUID recipient;
	private String destinationAddress;
}
