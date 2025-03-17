package com.eighteenthstreet.slack_service.presentation.dto;

public record SendMessageRequestDto(
	String receiverId,
	String message
) {
}
