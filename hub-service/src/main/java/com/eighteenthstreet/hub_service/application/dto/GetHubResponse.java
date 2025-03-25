package com.eighteenthstreet.hub_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.hub_service.domain.model.Hub;
import com.eighteenthstreet.hub_service.domain.model.HubCache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetHubResponse {

	private UUID hubId;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private UUID userId;

	public static GetHubResponse from(Hub hub) {
		return GetHubResponse.builder()
			.hubId(hub.getHubId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.userId(hub.getUserId())
			.build();
	}

	public static GetHubResponse fromCahce(HubCache hub) {
		return GetHubResponse.builder()
			.hubId(hub.getHubId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.userId(hub.getUserId())
			.build();
	}
}
