package com.eighteenthstreet.user_service.presentation.exceptions;

import exception.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() { // 반환 타입을 int → ErrorCode 변경
		return errorCode;
	}
}