package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderItemNotFoundException extends CustomException {
	public CustomOrderItemNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
