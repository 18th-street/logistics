package com.eighteenthstreet.hub_service.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_service.application.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}
