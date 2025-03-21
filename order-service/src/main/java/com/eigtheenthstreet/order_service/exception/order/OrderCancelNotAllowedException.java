package com.eigtheenthstreet.order_service.exception.order;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class OrderCancelNotAllowedException extends CustomException {
	public OrderCancelNotAllowedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
