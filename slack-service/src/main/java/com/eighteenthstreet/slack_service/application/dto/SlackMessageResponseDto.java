package com.eighteenthstreet.slack_service.application.dto;

public record SlackMessageResponseDto(
	String receiverId,
	String message
) {
}
