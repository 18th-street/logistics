package com.eighteenthstreet.user_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.user_service.domain.model.Status;

import auth.Role;

public record UserResponseDto(
	UUID userId,
	String username,
	String password,
	String email,
	String name,
	String phone,
	String slackId,
	Role role,
	Status status
) {
}
