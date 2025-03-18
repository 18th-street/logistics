package com.eigtheenthstreet.order_service.exception;

import exception.ErrorCode;

public class CustomCompanyNotSupplierException extends CustomException {
	public CustomCompanyNotSupplierException(ErrorCode errorCode) {
		super(errorCode);
	}
}
