package com.eighteenthstreet.slack_service.application.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eighteenthstreet.slack_service.application.dto.UserResponseDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {
	@GetMapping("/api/v1/users/incall/detail/{userId}")
	UserResponseDto getUser(
		@PathVariable("userId") UUID userId,
		@RequestHeader("X-Internal-Call") String internalHeader // 추가
	);
}
