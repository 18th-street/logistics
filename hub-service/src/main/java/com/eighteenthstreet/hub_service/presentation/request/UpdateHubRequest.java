package com.eighteenthstreet.hub_service.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHubRequest {

	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
}
