package com.eighteenthstreet.hub_route_service.exception;

import com.eighteenthstreet.hub_service.exception.CustomException;

import exception.ErrorCode;

public class CustomHubRouteNotFoundException extends CustomException {

	public CustomHubRouteNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
