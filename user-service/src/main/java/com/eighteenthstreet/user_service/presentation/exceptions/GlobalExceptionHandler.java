package com.eighteenthstreet.user_service.presentation.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exception.ErrorCode;
import exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 커스텀 비즈니스 예외 (서비스에서 발생하는 예외)
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error(" BusinessException 발생: {}", e.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
		return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
	}

	// 잘못된 인자(요청) 예외
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error(" 잘못된 요청: {}", e.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST);
		return new ResponseEntity<>(errorResponse, ErrorCode.INVALID_REQUEST.getHttpStatus());
	}

	// NullPointerException 등 예기치 못한 예외 처리
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		log.error(" 서버 내부 오류 발생: {}", e.getMessage(), e);
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Validation 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
	public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(Exception ex) {
		log.error("권한 관련 예외 발생: {}", ex.getMessage());
		return ResponseEntity
			.status(ErrorCode.ACCESS_DENIED.getHttpStatus())
			.body(new ErrorResponse(ErrorCode.ACCESS_DENIED));
	}

	// 최상위 Exception 핸들러 (그 외 모든 예외)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
		log.error(" 예상치 못한 예외 발생: {}", e.getMessage(), e);
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
