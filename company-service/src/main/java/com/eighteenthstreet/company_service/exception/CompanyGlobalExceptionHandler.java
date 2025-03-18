package com.eighteenthstreet.company_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exception.ErrorResponse;

@RestControllerAdvice
public class CompanyGlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(new ErrorResponse(e.getErrorCode()));
	}
}
