package com.eighteenthstreet.deliveryagentservice.domain.exception;

import com.eighteenthstreet.deliveryagentservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryagentservice.presentation.exception.error.CustomException;

public class DeliveryAgentNotFoundException extends CustomException {
    public DeliveryAgentNotFoundException(ErrorCode code) {
        super(code);
    }
}
