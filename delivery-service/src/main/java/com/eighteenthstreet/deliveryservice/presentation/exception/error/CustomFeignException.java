package com.eighteenthstreet.deliveryservice.presentation.exception.error;

public class CustomFeignException extends RuntimeException {
	private final String code;
	private final String message;

	public CustomFeignException(String code, String message) {
		super(code + ": " + message);
		this.code = code;
		this.message = message;
	}

	public CustomFeignException(String message) {
		super(message);
		this.code = "UNKNOWN_ERROR";
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
