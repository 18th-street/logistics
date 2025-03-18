package com.eighteenthstreet.hub_service.exception;

import exception.ErrorCode;

public class CustomHubAlreadyExistException extends CustomException {

	public CustomHubAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
