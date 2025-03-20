package com.eighteenthstreet.deliveryrouteservice.application.client;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetHubRouteResponse {
	private UUID hubRouteId;
	private Hub departureHub;  // UUID -> Hub 객체
	private Hub arrivalHub;    // UUID -> Hub 객체
	private Double estimatedDistance;
	private Double estimatedDuration;
	private String status;
}
