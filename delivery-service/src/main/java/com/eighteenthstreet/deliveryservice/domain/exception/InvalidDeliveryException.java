package com.eighteenthstreet.deliveryservice.domain.exception;

import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomException;

public class InvalidDeliveryException extends CustomException {
	public InvalidDeliveryException(ErrorCode code) {
		super(code);
	}
}
