package com.eighteenthstreet.product_service.exception;

import exception.ErrorCode;

public class CustomGetCompanyApiFailException extends CustomException {
	public CustomGetCompanyApiFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
