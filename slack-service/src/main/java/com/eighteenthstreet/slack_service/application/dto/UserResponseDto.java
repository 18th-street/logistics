package com.eighteenthstreet.slack_service.application.dto;

import java.util.UUID;

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
	public enum Status {
		WAITING, COMPLETE
	}
}
