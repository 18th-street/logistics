package com.eighteenthstreet.product_service.exception;

import exception.ErrorCode;

public class CustomProductNotFoundException extends CustomException {
	public CustomProductNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
