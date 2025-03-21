package com.eighteenthstreet.slack_service.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GetHubResponse(
	UUID hubId,
	String name,
	String address,
	BigDecimal longitude,
	BigDecimal latitude,
	UUID userId
) {
}
