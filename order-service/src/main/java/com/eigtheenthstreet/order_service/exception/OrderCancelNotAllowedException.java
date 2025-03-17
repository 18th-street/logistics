package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class OrderCancelNotAllowedException extends CustomException {
	public OrderCancelNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
