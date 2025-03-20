package com.eighteenthstreet.hub_route_service.application.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.eighteenthstreet.hub_route_service.domain.model.HubRoute;

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
	private HubDto departureHub;
	private HubDto arrivalHub;
	private Double estimatedDistance;
	private Double estimatedDuration;
	private String status;

	public static GetHubRouteResponse from(HubRoute hubRoute) {
		return GetHubRouteResponse.builder()
			.hubRouteId(hubRoute.getHubRouteId())
			.departureHub(HubDto.from(hubRoute.getDepartureHubId()))
			.arrivalHub(HubDto.from(hubRoute.getArrivalHubId()))
			.estimatedDistance(hubRoute.getEstimatedDistance())
			.estimatedDuration(hubRoute.getEstimatedDuration())
			.status(hubRoute.getStatus())
			.build();
	}

	public static List<GetHubRouteResponse> fromList(List<HubRoute> hubRoutes) {
		return hubRoutes.stream()
			.map(GetHubRouteResponse::from)
			.collect(Collectors.toList());
	}
}
