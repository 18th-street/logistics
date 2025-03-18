package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomInsufficientStockException extends CustomException {
	public CustomInsufficientStockException(ErrorCode errorCode) {
		super(errorCode);
	}
}
