package com.eighteenthstreet.hub_route_service.exception;

import exception.ErrorCode;

public class CustomHubRouteAlreadyExistsException extends CustomException {
	
	public CustomHubRouteAlreadyExistsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
