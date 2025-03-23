package com.eighteenthstreet.slack_service.infrastructure.rabbitmq;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.slack_service.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.slack_service.application.dto.GetHubResponse;
import com.eighteenthstreet.slack_service.application.dto.OrderDeliveryInfo;
import com.eighteenthstreet.slack_service.application.dto.SelectOrderResponse;
import com.eighteenthstreet.slack_service.application.dto.UserResponseDto;
import com.eighteenthstreet.slack_service.application.service.SlackService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageListener {
	private final SlackService slackService;

	@RabbitListener(queues = "${message.queue.slack}")
	public void sendSlackToHubManager(UUID orderId) {
		// order 정보
		SelectOrderResponse order = slackService.getOrder(orderId);
		log.info("주문 요청 : {}", order.deliveryLimitedAt());

		// delivery 정보
		DeliveryDetailsResponse delivery = slackService.getDelivery(order.deliveryId());
		log.info("배달 목적지 : {}", delivery.destinationAddress());

		// hub 정보
		List<GetHubResponse> hubs = slackService.getHubByIds(delivery.getSortedDeliveryRoute());
		log.info("첫 번째 허브 이름 ({}/{}): {}", 1, hubs.size(), hubs.get(0).name());

		// User 정보
		UserResponseDto user = slackService.getUser(hubs.get(0).userId());
		log.info("허브관리자 : {}", user.username());

		OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
			orderId,
			order.orderItems().stream().map(SelectOrderResponse.SelectOrderItemResponse::productName).toList(),
			order.orderItems().stream().map(SelectOrderResponse.SelectOrderItemResponse::productQuantity).toList(),
			order.deliveryLimitedAt(),
			hubs.get(0).name(),
			hubs.stream().map(GetHubResponse::name).toList(),
			delivery.destinationAddress()
		);
		// gemini->slack
		slackService.sendSlackMessageToManager(user.email(), orderDeliveryInfo);
	}

}
