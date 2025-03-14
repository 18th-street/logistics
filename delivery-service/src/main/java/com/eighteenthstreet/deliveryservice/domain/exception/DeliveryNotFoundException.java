package com.eighteenthstreet.deliveryservice.domain.exception;

import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomException;

public class DeliveryNotFoundException extends CustomException {
	public DeliveryNotFoundException(ErrorCode code) {
		super(code);
	}
}
