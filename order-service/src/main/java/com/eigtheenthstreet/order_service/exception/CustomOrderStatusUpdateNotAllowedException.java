package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomOrderStatusUpdateNotAllowedException extends CustomException {
	public CustomOrderStatusUpdateNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
