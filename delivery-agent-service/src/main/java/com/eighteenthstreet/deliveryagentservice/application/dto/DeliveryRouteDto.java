package com.eighteenthstreet.deliveryagentservice.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRouteDto {
	private UUID routeId;
	private int routeSequence;
	private UUID startHubId;
	private UUID endHubId;
}