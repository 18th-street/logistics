package com.eighteenthstreet.slack_service.presentation.dto;

public record SendMessageByEmailRequestDto(
	String receiverEmail,
	String message
) {
}
