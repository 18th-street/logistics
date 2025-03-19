package com.eighteenthstreet.hub_route_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.hub_service.domain.model.Hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class HubDto {

	private UUID hubId;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;

	public static HubDto from(Hub hub) {
		return HubDto.builder()
			.hubId(hub.getHubId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.build();
	}
}
