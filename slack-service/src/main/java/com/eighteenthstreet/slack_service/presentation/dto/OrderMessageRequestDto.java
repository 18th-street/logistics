package com.eighteenthstreet.slack_service.presentation.dto;

import java.util.UUID;

public record OrderMessageRequestDto(
	UUID orderId,
	UUID ordererId,
	String productName,
	Integer quantity,
	String requestDetails
) {
}
