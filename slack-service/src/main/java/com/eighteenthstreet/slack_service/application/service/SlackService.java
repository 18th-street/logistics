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
import com.eighteenthstreet.slack_service.infrastructure.Gemini.AiService;
import com.eighteenthstreet.slack_service.infrastructure.slack.SlackClient;
import com.eighteenthstreet.slack_service.presentation.dto.DeliveryMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.OrderMessageRequestDto;
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
	private final AiService aiService;

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
			throw new CustomException(ErrorCode.SLACK_ID_EXTRACT_FAILED);
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
			throw new CustomException(ErrorCode.SLACK_TRANSACTION_FAILED);
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

	@Transactional
	public void deleteSlackMessage(UUID id) {
		SlackMessage slackMessage = slackMessageRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.SLACK_NOT_FOUND));
		slackMessage.performSoftDelete();
	}

	@Transactional
	public void sendSlackMessageToHubManager(String slackId, OrderMessageRequestDto orderDto,
		DeliveryMessageRequestDto deliveryDto) {
		// email 이라고 가정하고,....
		String receiverId = slackClient.getSlackIdByEmail(slackId);
		if (receiverId == null) {
			throw new CustomException(ErrorCode.SLACK_ID_EXTRACT_FAILED);
		}
		// Gemini API
		String geminiResponse = aiService.getFinalShippingDeadline(orderDto, deliveryDto);

		// 문자 포멧
		String message = formatSlackMessage(orderDto, deliveryDto, geminiResponse);
		boolean isSent = slackClient.sendMessage(receiverId, message);
		if (isSent) {
			saveSlackMessage(receiverId, message);
		} else {
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}

	}

	private String formatSlackMessage(OrderMessageRequestDto order, DeliveryMessageRequestDto delivery,
		String geminiResponse) {
		String deadline = aiService.extractFinalDeadlineMessage(geminiResponse);
		return String.format(
			"*📢 발송 허브 담당자 알림 📢*\n" +
				"*📌 주문 번호:* %s\n" +
				"*📦 상품 정보:* %s (수량: %d)\n" +
				"*📝 요청 사항:* %s\n" +
				"*🚚 발송지:* %s\n" +
				"*🔁 경유지:* %s\n" +
				"*🎯 도착지:* %s\n" +
				"*📮 배송 담당자:* %s\n" +
				"--------------------------------------\n" +
				"📢 *최종 발송 시한:* %s",
			order.orderId(), order.productName(), order.quantity(),
			order.requestDetails(), delivery.startHub(), delivery.endHub(),
			delivery.destinationAddress(), delivery.userId(), deadline
		);
	}
}
