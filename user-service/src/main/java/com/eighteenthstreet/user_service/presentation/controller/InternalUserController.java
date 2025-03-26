package com.eighteenthstreet.user_service.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.user_service.application.dto.UserResponseDto;
import com.eighteenthstreet.user_service.application.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Hidden
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUserForInternal(@PathVariable("userId") UUID userId) {
		return ResponseEntity.ok(userService.getUserDetailInternal(userId));
	}
}