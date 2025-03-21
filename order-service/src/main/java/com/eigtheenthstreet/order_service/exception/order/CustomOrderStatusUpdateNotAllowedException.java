package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomOrderStatusUpdateNotAllowedException extends CustomException {
	public CustomOrderStatusUpdateNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
