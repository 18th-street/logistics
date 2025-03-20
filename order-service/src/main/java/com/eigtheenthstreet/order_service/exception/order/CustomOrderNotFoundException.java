package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderNotFoundException extends CustomException {
	public CustomOrderNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
