package com.eighteenthstreet.slack_service.presentation.controller;

import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.slack_service.application.service.SlackService;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slacks")
public class SlackController {
	private final SlackService slackService;

	@Description("슬랙 멤버 ID로 슬랙 메시지 전송")
	@PostMapping
	public ResponseEntity<Void> sendMessageToId(@RequestBody SendMessageRequestDto request) {
		slackService.sendSlackMessage(request);
		return ResponseEntity.ok().build();
	}

	@Description("email로 슬랙 메시지 전송")
	@PostMapping("/email")
	public ResponseEntity<Void> sendMessageByEmail(@RequestBody SendMessageByEmailRequestDto request) {
		slackService.sendSlackMessageByEmail(request);
		return ResponseEntity.ok().build();
	}

}
