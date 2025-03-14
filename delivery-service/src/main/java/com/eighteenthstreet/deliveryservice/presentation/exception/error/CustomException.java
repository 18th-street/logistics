package com.eighteenthstreet.deliveryservice.presentation.exception.error;

import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}