package com.eighteenthstreet.slack_service.presentation.dto;

import java.util.UUID;

public record DeliveryMessageRequestDto(
	String orderId,
	String startHub,
	String endHub,
	String destinationAddress,
	UUID userId
) {
}
