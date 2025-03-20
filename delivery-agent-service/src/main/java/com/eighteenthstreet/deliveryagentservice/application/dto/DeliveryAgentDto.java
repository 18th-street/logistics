package com.eighteenthstreet.deliveryagentservice.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAgentDto {
	private UUID deliveryAgentId;
	private String status;
	private int agentSequence;
	private DeliveryRouteDto route;
}
