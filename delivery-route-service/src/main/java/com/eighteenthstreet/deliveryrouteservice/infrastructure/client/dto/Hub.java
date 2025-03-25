package com.eighteenthstreet.deliveryrouteservice.infrastructure.client.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Hub {
	private UUID hubId;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
}