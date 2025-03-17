package com.eighteenthstreet.user_service.application.dto;

import com.eighteenthstreet.user_service.domain.model.Status;

public record UserResponseDto(
	Long userId,
	String username,
	String password,
	String slackId,
	String name,
	String phone,
	Role role,
	Status status
) {
}
