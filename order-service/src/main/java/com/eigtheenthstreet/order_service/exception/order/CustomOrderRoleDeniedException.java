package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderRoleDeniedException extends CustomException {
	public CustomOrderRoleDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
