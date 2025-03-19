package com.eighteenthstreet.company_service.application.dto;

import java.util.UUID;

public record GetHubResponse(
	UUID hubId,
	String name,
	String address
) {
}
