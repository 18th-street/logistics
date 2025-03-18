package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomOrderNotFoundException extends CustomException {
	public CustomOrderNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
