package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomCompanyNotReceiverException extends CustomException {
	public CustomCompanyNotReceiverException(ErrorCode errorCode) {
		super(errorCode);
	}
}
