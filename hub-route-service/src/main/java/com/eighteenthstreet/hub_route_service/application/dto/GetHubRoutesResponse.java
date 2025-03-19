package com.eighteenthstreet.hub_route_service.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetHubRoutesResponse {

	private List<GetHubRouteResponse> routes;

	public static GetHubRoutesResponse from(List<GetHubRouteResponse> routes) {
		return new GetHubRoutesResponse(routes);
	}
}
