package com.eighteenthstreet.deliveryservice.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryDetailsResponse {
	private UUID deliveryId;
	private String destinationAddress;
	private List<DeliveryAgentDto> deliveryAgents;

	public DeliveryDetailsResponse(UUID deliveryId, String destinationAddress, List<DeliveryAgentDto> deliveryAgents) {
		this.deliveryId = deliveryId;
		this.destinationAddress = destinationAddress;
		this.deliveryAgents = deliveryAgents;
	}

}
