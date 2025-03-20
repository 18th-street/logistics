package com.eighteenthstreet.deliveryrouteservice.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryrouteservice.application.client.GetHubRouteResponse;
import com.eighteenthstreet.deliveryrouteservice.application.client.GetHubRoutesResponse;
import com.eighteenthstreet.deliveryrouteservice.application.client.HubRouteClient;
import com.eighteenthstreet.deliveryrouteservice.application.dto.GetDeliveryRouteResponse;
import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryrouteservice.domain.exception.DeliveryRouteNotFoundException;
import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;
import com.eighteenthstreet.deliveryrouteservice.domain.repository.DeliveryRouteRepository;

import exception.ErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryRouteService {

	@Value("${message.queue.route}")
	private String queue;

	@Value("${message.queue.failed}")
	private String failedQueue;

	private final DeliveryRouteRepository deliveryRouteRepository;
	private final RabbitTemplate rabbitTemplate;
	private final HubRouteClient hubRouteClient;

	@RabbitListener(queues = "${message.queue.delivery}")
	@Transactional
	public void createDeliveryRoute(DeliveryCreatedEvent event) {
		log.info("######### 배송 요청 받음: {}", event);

		try {
			// 1. API로 경로 물어보기
			log.info("API 호출 시작: startHubId={}, endHubId={}", event.getStartHubId(), event.getEndHubId());
			GetHubRoutesResponse response = hubRouteClient.getHubRoutes(event.getStartHubId(), event.getEndHubId());
			log.info("API 호출 성공: response={}", response);

			// 2. 경로 저장하고, 배차 요청 준비하기
			List<RouteCreatedEvent.RouteInfo> routeInfos = new ArrayList<>();
			int sequence = 1;
			for (GetHubRouteResponse route : response.getRoutes()) {
				DeliveryRoute deliveryRoute = DeliveryRoute.builder()
					.deliveryId(event.getDeliveryId())
					.sequence(sequence)
					.startHubId(route.getDepartureHub().getHubId())
					.endHubId(route.getArrivalHub().getHubId())
					.estimatedDistance(route.getEstimatedDistance())
					.estimatedDuration(route.getEstimatedDuration())
					.build();
				deliveryRouteRepository.save(deliveryRoute);
				log.info("경로 저장: {}", deliveryRoute);

				routeInfos.add(
					new RouteCreatedEvent.RouteInfo(sequence++, route.getDepartureHub().getHubId(),
						route.getArrivalHub().getHubId()));
			}

			// 3. 배차 시작하라고 메시지 보내기
			RouteCreatedEvent routeEvent = new RouteCreatedEvent(event.getDeliveryId(), routeInfos);
			rabbitTemplate.convertAndSend(queue, routeEvent);
			log.info("##### 배차 요청 보냄: {}", routeEvent);
		} catch (FeignException.NotFound e) {
			log.error("경로를 찾을 수 없음: startHubId={}, endHubId={}, 오류: {}",
				event.getStartHubId(), event.getEndHubId(), e.getMessage());
			sendFailureEvent(event.getDeliveryId(), ErrorCode.DELIVERY_ROUTE_NOT_FOUND);
		} catch (Exception e) {
			log.error("예상치 못한 오류 발생: event={}, 오류: {}", event, e.getMessage(), e);
			sendFailureEvent(event.getDeliveryId(), ErrorCode.DELIVERY_ROUTE_CREATION_FAILED);
			throw e;
		}
	}

	public GetDeliveryRouteResponse getDeliveryRoutes(UUID deliveryAgentId) {
		DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryAgentId).orElseThrow(
			() -> new DeliveryRouteNotFoundException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND)
		);

		return GetDeliveryRouteResponse.fromEntity(deliveryRoute);
	}

	@Transactional
	public void deleteDeliveryRoute(UUID deliveryAgentId) {
		DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryAgentId).orElseThrow(
			() -> new DeliveryRouteNotFoundException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND)
		);

		deliveryRoute.softDelete();
	}

	private void sendFailureEvent(UUID deliveryId, ErrorCode errorCode) {
		DeliveryRouteCreationFailedEvent failedEvent = new DeliveryRouteCreationFailedEvent(deliveryId, errorCode);
		rabbitTemplate.convertAndSend(failedQueue, failedEvent);
		log.info("배송 경로 생성 실패 이벤트 발송: {}", failedEvent);
	}

	// 실패 이벤트 정의
	record DeliveryRouteCreationFailedEvent(UUID deliveryId, ErrorCode errorCode) {
	}

	// 이벤트 정의
	record RouteCreatedEvent(UUID deliveryId, List<RouteInfo> routes) {
		record RouteInfo(int sequence, UUID startHubId, UUID endHubId) {
		}
	}
}
