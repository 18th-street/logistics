package com.eighteenthstreet.hub_service.domain.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Hub")
public class HubCache {
	@Id
	private UUID hubId;

	private String name;

	private String address;

	private Double latitude;

	private Double longitude;

	private UUID userId;

	public static HubCache from(Hub hub) {
		return HubCache.builder()
			.hubId(hub.getHubId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.userId(hub.getUserId())
			.build();
	}
}
