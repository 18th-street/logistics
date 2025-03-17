package com.eighteenthstreet.hub_service.presentation.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHubRequest {

	private String name;
	private String address;
	private BigDecimal latitude;
	private BigDecimal longitude;
}
