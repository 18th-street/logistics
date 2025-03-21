package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomProductNotForSaleException extends CustomException {
	public CustomProductNotForSaleException(ErrorCode errorCode) {
		super(errorCode);
	}
}
