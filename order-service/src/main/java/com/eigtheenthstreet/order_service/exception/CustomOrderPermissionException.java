package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomOrderPermissionException extends CustomException {
	public CustomOrderPermissionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
