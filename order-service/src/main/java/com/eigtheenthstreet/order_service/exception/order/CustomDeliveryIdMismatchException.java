package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomDeliveryIdMismatchException extends CustomException {
	public CustomDeliveryIdMismatchException(ErrorCode errorCode) {
		super(errorCode);
	}
}
