package com.eighteenthstreet.deliveryagentservice.domain.exception;

import com.eighteenthstreet.deliveryagentservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryagentservice.presentation.exception.error.CustomException;

public class InvalidDeliveryAgentException extends CustomException {
    public InvalidDeliveryAgentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
