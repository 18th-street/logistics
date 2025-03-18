package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomProductNotForSaleException extends CustomException {
	public CustomProductNotForSaleException(ErrorCode errorCode) {
		super(errorCode);
	}
}
