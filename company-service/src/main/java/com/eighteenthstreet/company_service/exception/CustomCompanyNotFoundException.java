package com.eighteenthstreet.company_service.exception;

import exception.ErrorCode;

public class CustomCompanyNotFoundException extends CustomException {
	public CustomCompanyNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
