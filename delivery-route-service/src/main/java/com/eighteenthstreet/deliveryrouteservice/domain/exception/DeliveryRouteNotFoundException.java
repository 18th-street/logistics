package com.eighteenthstreet.deliveryrouteservice.domain.exception;

import com.eighteenthstreet.deliveryrouteservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryrouteservice.presentation.exception.error.CustomException;

public class DeliveryRouteNotFoundException extends CustomException {
    public DeliveryRouteNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
