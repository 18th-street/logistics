package com.eighteenthstreet.slack_service.application.dto;

import java.util.UUID;

public record SlackMessageResponseDto(
	UUID id,
	String receiverId,
	String message
) {
}
