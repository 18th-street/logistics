package com.eighteenthstreet.slack_service.application.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.slack_service.application.dto.UserResponseDto;

@FeignClient(name = "user-service", url = "http://localhost:19091")
public interface UserServiceClient {
	@GetMapping("/internal/users/{userId}")
	UserResponseDto getUserInternal(
		@PathVariable("userId") UUID userId
	);
}
