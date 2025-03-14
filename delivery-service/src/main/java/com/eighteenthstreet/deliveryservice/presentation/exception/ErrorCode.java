package com.eighteenthstreet.deliveryservice.presentation.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "해당 배달을 찾을 수 없습니다."),
	INVALID_DELIVERY(HttpStatus.NOT_FOUND, "D002", "해당 배달은 취소를 할 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
