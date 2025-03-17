package com.eighteenthstreet.slack_service.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;
import com.eighteenthstreet.slack_service.domain.repository.SlackMessageRepository;
import com.eighteenthstreet.slack_service.infrastructure.slack.SlackClient;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.exception.CustomException;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {
	private final SlackClient slackClient;
	private final SlackMessageRepository slackMessageRepository;

	@Transactional
	public void sendSlackMessage(SendMessageRequestDto request) {
		// Slack Bot을 통해 메시지 전송
		boolean isSent = slackClient.sendMessage(request);

		// 메시지 전송 성공 시, DB에 저장
		if (isSent) {
			SlackMessage slackMessage = SlackMessage.builder()
				.receiverId(request.receiverId())
				.message(request.message())
				.build();
			slackMessageRepository.save(slackMessage);
		} else {
			log.error(" Slack 메시지 전송 실패: receiverId={}, message={}", request.receiverId(), request.message());
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}
	}

	@Transactional
	public void sendSlackMessageByEmail(SendMessageByEmailRequestDto request) {
		// Slack Bot을 통해 메시지 전송
		boolean isSent = slackClient.sendMessageByEmail(request);

		// 메시지 전송 성공 시, DB에 저장
		if (isSent) {
			SlackMessage slackMessage = SlackMessage.builder()
				.receiverId(request.receiverEmail())
				.message(request.message())
				.build();
			slackMessageRepository.save(slackMessage);
		} else {
			log.error(" Slack 메시지 전송 실패: receiverId={}, message={}", request.receiverEmail(), request.message());
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}
	}

}
