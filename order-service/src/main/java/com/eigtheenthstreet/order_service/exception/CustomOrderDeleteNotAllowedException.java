package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomOrderDeleteNotAllowedException extends CustomException {
	public CustomOrderDeleteNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
