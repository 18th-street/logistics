package com.eighteenthstreet.company_service.exception;

import exception.ErrorCode;

public class CustomCompanyRoleDeniedException extends CustomException {
	public CustomCompanyRoleDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
