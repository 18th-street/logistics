package com.eighteenthstreet.deliveryagentservice.domain.exception;

import com.eighteenthstreet.deliveryagentservice.presentation.exception.error.CustomException;
import exception.ErrorCode;

public class DeliveryAgentNotFoundException extends CustomException {
    public DeliveryAgentNotFoundException(ErrorCode code) {
        super(code);
    }
}
