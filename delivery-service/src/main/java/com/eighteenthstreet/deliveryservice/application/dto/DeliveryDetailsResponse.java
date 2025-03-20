package com.eighteenthstreet.deliveryservice.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryDetailsResponse {
	private UUID deliveryId;
	private List<DeliveryAgentDto> deliveryAgents;

	public DeliveryDetailsResponse(UUID deliveryId, List<DeliveryAgentDto> deliveryAgents) {
		this.deliveryId = deliveryId;
		this.deliveryAgents = deliveryAgents;
	}

}
