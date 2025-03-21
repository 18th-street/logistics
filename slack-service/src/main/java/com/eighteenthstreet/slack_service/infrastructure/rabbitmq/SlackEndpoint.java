package com.eighteenthstreet.slack_service.infrastructure.rabbitmq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.eighteenthstreet.slack_service.application.client.DeliveryServiceClient;
import com.eighteenthstreet.slack_service.application.client.HubServiceClient;
import com.eighteenthstreet.slack_service.application.client.OrderServiceClient;
import com.eighteenthstreet.slack_service.application.client.UserServiceClient;
import com.eighteenthstreet.slack_service.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.slack_service.application.dto.GetHubResponse;
import com.eighteenthstreet.slack_service.application.dto.OrderDeliveryInfo;
import com.eighteenthstreet.slack_service.application.dto.SelectOrderResponse;
import com.eighteenthstreet.slack_service.application.dto.UserResponseDto;
import com.eighteenthstreet.slack_service.application.service.SlackService;
import com.eighteenthstreet.slack_service.infrastructure.config.ServletRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackEndpoint {
	private final OrderServiceClient orderServiceClient;
	private final DeliveryServiceClient deliveryServiceClient;
	private final HubServiceClient hubServiceClient;
	private final UserServiceClient userServiceClient;
	private final SlackService slackService;
	private final ServletRequestUtil util;

	// @RabbitListener(queues = "${message.queue.slack}")
	public void sendSlackToHubManager(UUID orderId) {
		String token = util.getAccessTokenFromHeader();
		log.info(" token: " + token);
		// order 정보
		// SelectOrderResponse order = orderServiceClient.getOrder(orderId);
		SelectOrderResponse order = new SelectOrderResponse(
			UUID.randomUUID(), // orderId
			UUID.randomUUID(), // ordererId
			UUID.randomUUID(), // supplierCompanyId
			UUID.randomUUID(), // consumerCompanyId
			15, // orderTotalQuantity
			250000, // orderTotalAmount
			"2025년 04월 04일 22시 50분", // deliveryLimitedAt
			List.of(
				new SelectOrderResponse.SelectOrderItemResponse(
					UUID.randomUUID(), "크록스 블랙", 10, 15000, 150000
				),
				new SelectOrderResponse.SelectOrderItemResponse(
					UUID.randomUUID(), "크록스 화이트", 5, 20000, 100000
				)
			),
			UUID.randomUUID(), // deliveryId
			SelectOrderResponse.OrderStatus.CREATED
		);

		// delivery 정보
		// GetDeliveryResponse delivery = deliveryServiceClient.getDelivery(order.deliveryId());
		// UUID hub1 = UUID.fromString("e4f1b6e5-1a8e-4d3f-bb7c-1f6d1f2e7a3d");
		// UUID hub2 = UUID.fromString("0ab1c2d3-e4f5-4789-abcd-abcdefabcdef");
		// UUID hub3 = UUID.fromString("12ab34cd-56ef-4789-90ab-abcdefabcdef");

		UUID hub1 = UUID.randomUUID();
		UUID hub2 = UUID.randomUUID();
		UUID hub3 = UUID.randomUUID();

		DeliveryDetailsResponse.DeliveryRouteDto route1 = new DeliveryDetailsResponse.DeliveryRouteDto(
			UUID.randomUUID(), 1, hub1, hub2
		);
		DeliveryDetailsResponse.DeliveryRouteDto route2 = new DeliveryDetailsResponse.DeliveryRouteDto(
			UUID.randomUUID(), 2, hub2, hub3
		);

		DeliveryDetailsResponse.DeliveryAgentDto agent1 = new DeliveryDetailsResponse.DeliveryAgentDto(
			UUID.randomUUID(), "IN_PROGRESS", 1, route1
		);
		DeliveryDetailsResponse.DeliveryAgentDto agent2 = new DeliveryDetailsResponse.DeliveryAgentDto(
			UUID.randomUUID(), "WAITING", 2, route2
		);

		DeliveryDetailsResponse delivery = new DeliveryDetailsResponse(
			UUID.randomUUID(),
			"부산시 해운대구 센텀중앙로 97",
			List.of(agent1, agent2)
		);

		// hub 정보
		// List<GetHubResponse> hubs = hubServiceClient.getHubsByIds(delivery.getSortedDeliveryRoute());
		// log.info("허브정보 다 가져옴");

		// 🏢 Hub 데이터 생성
		List<GetHubResponse> hubs = new ArrayList<>();

		GetHubResponse startHub = new GetHubResponse(
			hub1, "서울특별시 센터", "서울특별시 송파구 송파대로 55",
			BigDecimal.valueOf(37.514575), BigDecimal.valueOf(127.105399), UUID.randomUUID()
		);

		GetHubResponse midHub = new GetHubResponse(
			hub2, "대구광역시 센터", "대구 북구 태평로 161",
			BigDecimal.valueOf(35.889811), BigDecimal.valueOf(128.612779), UUID.randomUUID()
		);

		GetHubResponse endHub = new GetHubResponse(
			hub3, "부산광역시 센터", "부산 동구 중앙대로 206",
			BigDecimal.valueOf(35.115201), BigDecimal.valueOf(129.041328), UUID.randomUUID()
		);

		hubs.add(startHub);
		hubs.add(midHub);
		hubs.add(endHub);

		// User 정보
		// UserResponseDto user = userServiceClient.getUser(hubs.get(0).userId(), token);
		UserResponseDto user = userServiceClient.getUser(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
			token);
		String email = user.email();
		log.info("허브관리자 정보 가져옴");

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
		slackService.sendSlackMessageToManager(email, orderDeliveryInfo);
	}

}
