package com.eighteenthstreet.hub_service.exception;

import exception.ErrorCode;

public class CustomHubNotFoundException extends CustomException {

	public CustomHubNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
