package com.eighteenthstreet.product_service.exception;

import exception.ErrorCode;

public class CustomProductAlreadyExistException extends CustomException {
	public CustomProductAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
