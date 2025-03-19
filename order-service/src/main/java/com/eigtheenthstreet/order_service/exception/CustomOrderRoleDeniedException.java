package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomOrderRoleDeniedException extends CustomException {
	public CustomOrderRoleDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
