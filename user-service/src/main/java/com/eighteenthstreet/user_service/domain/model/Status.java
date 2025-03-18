package com.eighteenthstreet.user_service.domain.model;

import lombok.Getter;

@Getter
public enum Status {
	WAITING("인증 대기 중"),
	COMPLETE("인증 완료");

	private final String description;

	Status(String description) {
		this.description = description;
	}
}