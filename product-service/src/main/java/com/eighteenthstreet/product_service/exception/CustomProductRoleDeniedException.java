package com.eighteenthstreet.product_service.exception;

import exception.ErrorCode;

public class CustomProductRoleDeniedException extends CustomException {
	public CustomProductRoleDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
