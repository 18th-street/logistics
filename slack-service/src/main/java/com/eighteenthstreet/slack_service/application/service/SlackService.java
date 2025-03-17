package com.eighteenthstreet.slack_service.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.slack_service.application.dto.SlackMessageResponseDto;
import com.eighteenthstreet.slack_service.domain.model.SlackMessage;
import com.eighteenthstreet.slack_service.domain.repository.SlackMessageRepository;
import com.eighteenthstreet.slack_service.infrastructure.slack.SlackClient;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.UpdateSlackMessageRequestDto;
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
	private final SlackMessageMapper slackMessageMapper;

	@Transactional
	public void sendSlackMessage(SendMessageRequestDto request) {
		boolean isSent = slackClient.sendMessage(request.receiverId(), request.message());
		if (isSent) {
			saveSlackMessage(request.receiverId(), request.message());
		} else {
			log.error(" Slack 메시지 전송 실패: receiverId={}, message={}", request.receiverId(), request.message());
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}
	}

	@Transactional
	public void sendSlackMessageByEmail(SendMessageByEmailRequestDto request) {
		String receiverId = slackClient.getSlackIdByEmail(request.receiverEmail());
		if (receiverId == null) {
			throw new CustomException(ErrorCode.EXTRACT_ID_FAILED);
		}
		boolean isSent = slackClient.sendMessage(receiverId, request.message());
		if (isSent) {
			saveSlackMessage(receiverId, request.message());
		} else {
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}
	}

	private void saveSlackMessage(String receiverId, String message) {
		try {
			SlackMessage slackMessage = SlackMessage.builder()
				.receiverId(receiverId)
				.message(message)
				.build();
			slackMessageRepository.save(slackMessage);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.SL_TRANSACTION_FAILED);
		}
	}

	public List<SlackMessageResponseDto> getAllSlackMessages() {
		return slackMessageRepository.findAll().stream()
			.map(slackMessageMapper::toDto).toList();
	}

	public SlackMessageResponseDto getSlackMessages(UUID id) {
		SlackMessage slack = slackMessageRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.SLACK_NOT_FOUND));
		return slackMessageMapper.toDto(slack);
	}

	public Page<SlackMessageResponseDto> searchSlackMessages(String word, Pageable customPageable) {
		return slackMessageRepository.findAllByMessageContains(word, customPageable).map(slackMessageMapper::toDto);
	}

	@Transactional
	public SlackMessageResponseDto updateSlackMessage(UUID id, UpdateSlackMessageRequestDto request) {
		SlackMessage slackMessage = slackMessageRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.SLACK_NOT_FOUND));
		slackMessage.update(request);
		return slackMessageMapper.toDto(slackMessage);
	}
}
