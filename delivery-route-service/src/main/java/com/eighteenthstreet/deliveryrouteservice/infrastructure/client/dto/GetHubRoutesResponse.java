package com.eighteenthstreet.deliveryrouteservice.infrastructure.client.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetHubRoutesResponse {

	private List<GetHubRouteResponse> routes;
}