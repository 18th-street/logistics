package com.eighteenthstreet.deliveryrouteservice.presentation.exception.error;

import com.eighteenthstreet.deliveryrouteservice.presentation.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}