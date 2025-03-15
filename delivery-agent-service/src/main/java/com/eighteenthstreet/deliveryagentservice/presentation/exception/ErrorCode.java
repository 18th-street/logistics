package com.eighteenthstreet.deliveryagentservice.presentation.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DELIVERY_AGENT_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "해당 배달담당자를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
