package com.eighteenthstreet.product_service.exception;

import exception.ErrorCode;

public class CustomMismatchHubIdException extends CustomException {
	public CustomMismatchHubIdException(ErrorCode errorCode) {
		super(errorCode);
	}
}
