package com.eighteenthstreet.hub_route_service.exception;

import com.eighteenthstreet.hub_service.exception.CustomException;

import exception.ErrorCode;

public class CustomHubNotFoundException extends CustomException {

	public CustomHubNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
