package com.eighteenthstreet.deliveryagentservice.application;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eighteenthstreet.deliveryagentservice.domain.event.DeliveryAgentAssignedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.event.RouteCreatedEvent;
import com.eighteenthstreet.deliveryagentservice.domain.exception.InvalidDeliveryAgentException;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryDispatchService {
	private final DeliveryAgentRepository deliveryAgentRepository;
	private final AtomicInteger roundRobinIdx = new AtomicInteger(0);
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.delivery-assigned}")
	private String deliveryAssignedQueue;

	@RabbitListener(queues = "${message.queue.route}")
	public void handleRouteCratedEvent(RouteCreatedEvent event) {
		log.info("배달담당서비스 요청 받음: {}", event);

		try {

			// 1. 사용 가능한 배달 담당자 목록 조회
			List<DeliveryAgent> availableAgents = deliveryAgentRepository.findByDeliveryAgentStatus(
				DeliveryAgentStatus.AVAILABLE);
			if (availableAgents.isEmpty()) {
				throw new InvalidDeliveryAgentException(ErrorCode.INVALID_DELIVERY);

			}
			log.info("사용 가능한 담당자 수: {}", availableAgents.size());

			// 2. 라운드 로빈으로 경로 배치
			List<RouteCreatedEvent.RouteInfo> routes = event.routes();
			for (RouteCreatedEvent.RouteInfo route : routes) {
				// 라운드 로빈 인덱스 계산
				int agentIndex = roundRobinIdx.getAndIncrement() % availableAgents.size();
				DeliveryAgent assignedAgent = availableAgents.get(agentIndex);

				// 3. 배차 정보 업데이트
				assignedAgent.setDeliveryId(event.deliveryId());
				assignedAgent.setHubId(route.startHubId());
				assignedAgent.setSequence(route.sequence());
				assignedAgent.setDeliveryAgentStatus(DeliveryAgentStatus.IN_DELIVERY);

				deliveryAgentRepository.save(assignedAgent);
				log.info("경로 배차 완료: agentId = {}, route = {}", assignedAgent.getDeliveryAgentId(), route);

				DeliveryAgentAssignedEvent assignedEvent = new DeliveryAgentAssignedEvent(
					event.deliveryId(),
					assignedAgent.getDeliveryAgentId()
				);
				rabbitTemplate.convertAndSend(deliveryAssignedQueue, assignedEvent);
				log.info("배달 서비스에 이벤트 발송: {}", assignedEvent);
			}

		} catch (Exception e) {
			log.error("배차 처리 중 오류 발생: event={}, 오류: {}", event, e.getMessage(), e);
			throw e;  // 예외 메세지 처리
		}

	}

}

