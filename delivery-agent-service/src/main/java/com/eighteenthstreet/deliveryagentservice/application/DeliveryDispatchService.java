package com.eighteenthstreet.deliveryagentservice.application;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.eighteenthstreet.deliveryagentservice.application.event.DeliveryAgentEventPublisher;
import com.eighteenthstreet.deliveryagentservice.domain.event.DeliveryAgentAssignedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.event.DeliveryFailedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.event.RouteCreatedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.exception.InvalidDeliveryAgentException;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;
import com.eighteenthstreet.deliveryagentservice.infrastructure.client.HubFeignClient;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryDispatchService {
	private final DeliveryAgentRepository deliveryAgentRepository;
	private final AtomicInteger roundRobinIdx = new AtomicInteger(0);
	private final HubFeignClient hubFeignClient;
	private final DeliveryAgentEventPublisher deliveryAssignedQueue;
	private final DeliveryAgentEventPublisher deliveryAgentEventPublisher;

	@RabbitListener(queues = "${message.queue.delivery-route}")
	public void handleRouteCratedEvent(RouteCreatedEvent event) {
		log.info("배달담당서비스 요청 받음: {}", event);

		try {
			// 각 경로별로 배차
			List<RouteCreatedEvent.RouteInfo> routes = event.routes();
			for (RouteCreatedEvent.RouteInfo route : routes) {
				// 1. 사용 가능한 배달 담당자 목록 조회 (hubId 기반)
				List<DeliveryAgent> availableAgents = getAvailableAgentsForRoute(route);
				if (availableAgents.isEmpty()) {
					throw new InvalidDeliveryAgentException(ErrorCode.INVALID_DELIVERY_AGENT);
				}
				log.info("사용 가능한 담당자 수: {}", availableAgents.size());

				// 2. 라운드 로빈으로 경로 배치
				int agentIndex = roundRobinIdx.getAndIncrement() % availableAgents.size();
				DeliveryAgent assignedAgent = availableAgents.get(agentIndex);

				// 3. 배차 정보 업데이트
				assignedAgent.setDeliveryId(event.deliveryId());
				assignedAgent.setDeliveryRouteId(route.routeId());
				assignedAgent.setHubId(route.startHubId());
				assignedAgent.setSequence(route.sequence());
				assignedAgent.setDeliveryAgentStatus(DeliveryAgentStatus.IN_DELIVERY);

				deliveryAgentRepository.save(assignedAgent);
				log.info("경로 배차 완료: agentId = {}, route = {}", assignedAgent.getDeliveryAgentId(), route);
			}

			DeliveryAgentAssignedEvent assignedEvent = new DeliveryAgentAssignedEvent(event.deliveryId());
			deliveryAssignedQueue.publishDeliveryAgentAssignedEvent(assignedEvent);

		} catch (Exception e) {
			DeliveryFailedEvent failedEvent = new DeliveryFailedEvent(event.deliveryId(),
				ErrorCode.INVALID_DELIVERY_AGENT);
			deliveryAgentEventPublisher.publishDeliveryFailedEvent(failedEvent, e);
			throw e;  // 예외 메세지 처리
		}

	}

	private List<DeliveryAgent> getAvailableAgentsForRoute(RouteCreatedEvent.RouteInfo route) {
		UUID startHubId = route.startHubId();
		if (startHubId != null && hubFeignClient.existsById(startHubId)) {
			// 허브가 존재하면 hubId가 일치하는 HUB_AGENT 우선 조회
			List<DeliveryAgent> hubAgents = deliveryAgentRepository.findByDeliveryAgentStatusAndHubIdAndDeliveryAgentType(
				DeliveryAgentStatus.AVAILABLE, startHubId, DeliveryAgentType.HUB_AGENT);
			if (!hubAgents.isEmpty()) {
				return hubAgents;
			}
			// 허브가 있지만 HUB_AGENT가 없으면 COMPANY_AGENT도 허용
			return deliveryAgentRepository.findByDeliveryAgentStatus(DeliveryAgentStatus.AVAILABLE);
		}
		// startHubId가 없거나 허브가 없으면 COMPANY_AGENT만 조회
		return deliveryAgentRepository.findByDeliveryAgentStatusAndDeliveryAgentType(DeliveryAgentStatus.AVAILABLE,
			DeliveryAgentType.COMPANY_AGENT);
	}
}



