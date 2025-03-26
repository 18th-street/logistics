package com.eighteenthstreet.slack_service.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.slack_service.application.client.DeliveryServiceClient;
import com.eighteenthstreet.slack_service.application.client.HubServiceClient;
import com.eighteenthstreet.slack_service.application.client.OrderServiceClient;
import com.eighteenthstreet.slack_service.application.client.UserServiceClient;
import com.eighteenthstreet.slack_service.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.slack_service.application.dto.GetHubResponse;
import com.eighteenthstreet.slack_service.application.dto.OrderDeliveryInfo;
import com.eighteenthstreet.slack_service.application.dto.SelectOrderResponse;
import com.eighteenthstreet.slack_service.application.dto.SlackMessageResponseDto;
import com.eighteenthstreet.slack_service.application.dto.UserResponseDto;
import com.eighteenthstreet.slack_service.domain.model.SlackMessage;
import com.eighteenthstreet.slack_service.domain.repository.SlackMessageRepository;
import com.eighteenthstreet.slack_service.infrastructure.Gemini.AiService;
import com.eighteenthstreet.slack_service.infrastructure.config.ServletRequestUtil;
import com.eighteenthstreet.slack_service.infrastructure.messaging.message.NotificationEvent;
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
	private final AiService aiService;
	private final ServletRequestUtil util;
	private final SlackClient slackClient;
	private final HubServiceClient hubServiceClient;
	private final UserServiceClient userServiceClient;
	private final SlackMessageMapper slackMessageMapper;
	private final OrderServiceClient orderServiceClient;
	private final DeliveryServiceClient deliveryServiceClient;
	private final SlackMessageRepository slackMessageRepository;

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
	public void handleNotificationEvent(NotificationEvent event) {
		// order 정보
		SelectOrderResponse order = getOrder(event.orderId());
		log.info("주문 요청 : {}", order.deliveryLimitedAt());
		log.info("배송 ID : {}", order.deliveryId());

		// delivery 정보
		if (order.deliveryId() == null) {
			throw new CustomException(ErrorCode.DELIVERY_NOT_FOUND);
		}
		DeliveryDetailsResponse delivery = getDelivery(order.deliveryId());
		log.info("배달 목적지 : {}", delivery.destinationAddress());
		log.info("허브 Ids : {}", delivery.getSortedDeliveryRoute());

		// hub 정보
		List<GetHubResponse> hubs = getHubByIds(delivery.getSortedDeliveryRoute());
		log.info("허브 : {}", hubs);
		log.info("허브 개수 : {}", hubs.size());
		log.info("시작 허브 관리자 ID: {}", hubs.get(0).userId());

		// User 정보
		UserResponseDto user = getUser(hubs.get(0).userId());
		log.info("허브관리자 : {}", user.username());

		OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
			event.orderId(),
			order.orderItems().stream().map(SelectOrderResponse.SelectOrderItemResponse::productName).toList(),
			order.orderItems().stream().map(SelectOrderResponse.SelectOrderItemResponse::productQuantity).toList(),
			order.deliveryLimitedAt(),
			hubs.get(0).name(),
			hubs.stream().map(GetHubResponse::name).toList(),
			delivery.destinationAddress()
		);
		// gemini->slack
		sendSlackMessageToManager(user.email(), orderDeliveryInfo);

	}

	public SelectOrderResponse getOrder(UUID orderId) {
		try {
			return orderServiceClient.getOrder(orderId);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.ORDER_GET_API_FAIL);
		}
	}

	public DeliveryDetailsResponse getDelivery(UUID deliveryId) {
		try {
			return deliveryServiceClient.getDelivery(deliveryId);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.DELIVERY_GET_API_FAIL);
		}
	}

	public List<GetHubResponse> getHubByIds(List<UUID> hubIds) {
		try {
			return hubServiceClient.getHubsByIds(hubIds);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.HUB_GET_API_FAIL);
		}
	}

	public UserResponseDto getUser(UUID userId) {
		try {
			log.info("userId: {}", userId);
			// String token = util.getAccessTokenFromHeader();
			return userServiceClient.getUserInternal(userId);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.USER_GET_API_FAIL);
		}
	}

	public void sendSlackMessageToManager(String email, OrderDeliveryInfo request) {
		// email 가져옴
		String receiverId = slackClient.getSlackIdByEmail(email);
		log.info("슬랙 ID 추출 : {}", receiverId);
		if (receiverId == null) {
			throw new CustomException(ErrorCode.SLACK_ID_EXTRACT_FAILED);
		}
		// Gemini API
		String geminiResponse = aiService.getFinalShippingDeadline(request);

		// 문자 포멧
		String message = formatSlackMessage(request, geminiResponse);
		boolean isSent = slackClient.sendMessage(receiverId, message);
		if (isSent) {
			saveSlackMessage(receiverId, message);
		} else {
			throw new CustomException(ErrorCode.SLACK_SEND_FAILED);
		}

	}

	private String formatSlackMessage(OrderDeliveryInfo orderDeliveryInfo, String geminiResponse) {
		// Gemini 응답에서 최종 발송 시한 추출
		String deadline = aiService.extractFinalDeadlineMessage(geminiResponse);

		StringBuilder message = new StringBuilder();

		message.append("*📢 발송 허브 담당자 알림 📢*\n");
		message.append("*📌 주문 번호:* ").append(orderDeliveryInfo.orderId()).append("\n\n");

		message.append("*📦 상품 정보:*\n");
		for (int i = 0; i < orderDeliveryInfo.productName().size(); i++) {
			message.append("- ").append(orderDeliveryInfo.productName().get(i))
				.append(" (수량: ").append(orderDeliveryInfo.productQuantity().get(i)).append(")\n");
		}
		message.append("\n");

		if (orderDeliveryInfo.requests() != null && !orderDeliveryInfo.requests().isEmpty()) {
			message.append("*📝 고객 요청 배송 마감 시한:* ").append(orderDeliveryInfo.requests()).append("\n\n");
		}

		message.append("*🚚 발송지:* ").append(orderDeliveryInfo.start()).append("\n");

		if (!orderDeliveryInfo.stopping().isEmpty()) {
			message.append("*🔁 경유지:*\n");
			for (String hub : orderDeliveryInfo.stopping()) {
				message.append("- ").append(hub).append("\n");
			}
			message.append("\n");
		}

		message.append("*🎯 도착지:* ").append(orderDeliveryInfo.destination()).append("\n");
		message.append("--------------------------------------\n");
		message.append("📢 *최종 발송 시한:* ").append(deadline);

		return message.toString();
	}

	private void saveSlackMessage(String receiverId, String message) {
		try {
			SlackMessage slackMessage = SlackMessage.builder()
				.receiverId(receiverId)
				.message(message)
				.build();
			slackMessageRepository.save(slackMessage);
		} catch (CustomException e) {
			log.error("Slack 저장 중 예외 발생", e);
		}
	}
}
