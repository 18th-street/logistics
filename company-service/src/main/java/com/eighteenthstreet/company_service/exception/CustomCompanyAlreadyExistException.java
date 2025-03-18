package com.eighteenthstreet.company_service.exception;

import exception.ErrorCode;

public class CustomCompanyAlreadyExistException extends CustomException {
	public CustomCompanyAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
