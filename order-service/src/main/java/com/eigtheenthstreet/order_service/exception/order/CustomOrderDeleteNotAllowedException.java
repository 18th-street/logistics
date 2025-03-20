package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderDeleteNotAllowedException extends CustomException {
	public CustomOrderDeleteNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
