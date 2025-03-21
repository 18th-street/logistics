package com.eigtheenthstreet.order_service.exception.product;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomProductBulkNotFoundException extends CustomException {

	public CustomProductBulkNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
