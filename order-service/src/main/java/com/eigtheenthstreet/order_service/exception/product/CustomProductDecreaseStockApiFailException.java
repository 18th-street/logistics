package com.eigtheenthstreet.order_service.exception.product;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomProductDecreaseStockApiFailException extends CustomException {
	public CustomProductDecreaseStockApiFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
