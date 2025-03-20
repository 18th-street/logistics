package com.eighteenthstreet.hub_route_service.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateHubRouteRequest {
	private UUID departureHubId;
	private UUID arrivalHubId;
}
