package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderPermissionException extends CustomException {
	public CustomOrderPermissionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
