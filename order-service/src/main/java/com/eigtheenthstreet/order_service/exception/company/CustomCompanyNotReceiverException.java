package com.eigtheenthstreet.order_service.exception.company;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomCompanyNotReceiverException extends CustomException {
	public CustomCompanyNotReceiverException(ErrorCode errorCode) {
		super(errorCode);
	}
}
