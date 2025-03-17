package com.eighteenthstreet.hub_service.application.dto;

import java.math.BigDecimal;
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
public class CreateHubResponse {

	private UUID hubId;
	private String name;
	private String address;
	private BigDecimal latitude;
	private BigDecimal longitude;

	public static CreateHubResponse from(Hub hub) {
		return CreateHubResponse.builder()
			.hubId(hub.getHubId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.build();
	}
}
