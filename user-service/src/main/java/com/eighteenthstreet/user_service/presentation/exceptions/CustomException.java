package com.eighteenthstreet.user_service.presentation.exceptions;

import exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() { // 반환 타입을 int → ErrorCode 변경
		return errorCode;
	}
}