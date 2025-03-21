package com.eigtheenthstreet.order_service.exception.company;

import com.eigtheenthstreet.order_service.exception.CustomException;

import exception.ErrorCode;

public class CustomCompanyNotSupplierException extends CustomException {
	public CustomCompanyNotSupplierException(ErrorCode errorCode) {
		super(errorCode);
	}
}
