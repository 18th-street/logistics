package com.eighteenthstreet.deliveryservice.presentation.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomException;
import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomFeignException;

import exception.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ErrorResponse(e.getErrorCode()));
	}

	@ExceptionHandler(CustomFeignException.class)
	public ResponseEntity<Map<String, String>> handleCustomFeignException(CustomFeignException e) {
		Map<String, String> response = new HashMap<>();
		response.put("code", e.getCode());
		response.put("message", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}