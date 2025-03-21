package com.eigtheenthstreet.order_service.exception.company;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomCompanyGetApiFailException extends CustomException {
	public CustomCompanyGetApiFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
