package com.eighteenthstreet.company_service.exception;

import exception.ErrorCode;

public class CustomCompanyHubNotFoundException extends CustomException {
	public CustomCompanyHubNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
