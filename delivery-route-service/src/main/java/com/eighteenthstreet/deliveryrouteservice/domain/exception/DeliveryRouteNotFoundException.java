package com.eighteenthstreet.deliveryrouteservice.domain.exception;

import com.eighteenthstreet.deliveryrouteservice.presentation.exception.error.CustomException;
import exception.ErrorCode;

public class DeliveryRouteNotFoundException extends CustomException {
    public DeliveryRouteNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
