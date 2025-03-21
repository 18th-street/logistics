package com.eigtheenthstreet.order_service.exception.product;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomInsufficientStockException extends CustomException {
	public CustomInsufficientStockException(ErrorCode errorCode) {
		super(errorCode);
	}
}
