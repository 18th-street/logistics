package com.eighteenthstreet.user_service.application.dto;

public record TokenDto(
	String accessToken,
	String refreshToken
) {
}