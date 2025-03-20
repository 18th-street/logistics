package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomProductRestoreStockApiFailException extends CustomException {
	public CustomProductRestoreStockApiFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
