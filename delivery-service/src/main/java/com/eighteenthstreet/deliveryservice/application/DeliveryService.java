package com.eighteenthstreet.deliveryservice.application;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryservice.application.client.DeliveryAgentClient;
import com.eighteenthstreet.deliveryservice.application.client.DeliveryRouteClient;
import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryAgentDto;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryRouteDto;
import com.eighteenthstreet.deliveryservice.application.dto.GetDeliveryResponse;
import com.eighteenthstreet.deliveryservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.presentation.exception.error.CustomException;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;
	private final RabbitTemplate rabbitTemplate;
	private final DeliveryRouteClient deliveryRouteClient;
	private final DeliveryAgentClient deliveryAgentClient;

	@Value("${message.queue.delivery}")
	private String queueDelivery;

	public CreateDeliveryResponse createDelivery(CreateDeliveryRequest createDeliveryRequest) {
		Delivery delivery = Delivery.createDelivery(createDeliveryRequest);

		delivery = deliveryRepository.save(delivery);

		DeliveryCreatedEvent event = new DeliveryCreatedEvent(createDeliveryRequest.getStartHubId(),
			createDeliveryRequest.getEndHubId(), delivery.getDeliveryId());
		log.info("######### Send Message[Delivery] : {}", event);
		rabbitTemplate.convertAndSend(queueDelivery, event);
		return CreateDeliveryResponse.fromEntity(delivery);
	}

	@Transactional
	public void updateDeliveryStatus(UpdateStatusDeliveryRequest request) {
		Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
			.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.updateStatus(request.getDeliveryStatus());
		deliveryRepository.save(delivery);
	}

	//TODO: hubClient 에서 허브경로 받아와서 저장하고, 배달담당자도 받아와야함
	public GetDeliveryResponse getDelivery(UUID uuid) {
		Delivery delivery = deliveryRepository.findById(uuid)
			.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

		return GetDeliveryResponse.fromEntity(delivery);
	}

	@Transactional
	public void deleteDelivery(UUID deliveryId) {

		// 1. Delivery 조회 및 삭제
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.cancel();
		delivery.softDelete();

		try {
			// 2. DeliveryRoute 삭제 (Feign Client 호출)
			ResponseEntity<Map<String, String>> routeResponse = deliveryRouteClient.deleteDeliveryRouteByDeliveryId(
				deliveryId);
			if (!routeResponse.getStatusCode().is2xxSuccessful()) {
				throw new CustomException(ErrorCode.DELIVERY_ROUTE_DELETION_FAILED);
			}

			// 3. DeliveryAgent 삭제 (Feign Client 호출)
			ResponseEntity<Map<String, String>> agentResponse = deliveryAgentClient.deleteDeliveryAgentByDeliveryId(
				deliveryId);
			if (!agentResponse.getStatusCode().is2xxSuccessful()) {
				throw new CustomException(ErrorCode.DELIVERY_AGENT_DELETION_FAILED);
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}
	}

	public DeliveryDetailsResponse getDeliveryDetails(UUID deliveryId) {
		try {
			// Delivery 존재 여부 확인
			deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

			// 1. DeliveryAgent 조회
			ResponseEntity<List<DeliveryAgentDto>> agentResponse = deliveryAgentClient.getDeliveryAgentsByDeliveryId(
				deliveryId);
			List<DeliveryAgentDto> agentsWithoutRoutes =
				agentResponse.getBody() != null ? agentResponse.getBody() : Collections.emptyList();

			// 2. DeliveryRoute 조회
			ResponseEntity<List<DeliveryRouteDto>> routeResponse = deliveryRouteClient.getDeliveryRoutesByDeliveryId(
				deliveryId);
			List<DeliveryRouteDto> routes =
				routeResponse.getBody() != null ? routeResponse.getBody() : Collections.emptyList();

			// 3. Agent와 Route 매핑 (sequence 기준)
			Map<Integer, DeliveryRouteDto> routeMap = routes.stream()
				.collect(Collectors.toMap(DeliveryRouteDto::getRouteSequence, route -> route));
			List<DeliveryAgentDto> agentsWithRoutes = agentsWithoutRoutes.stream()
				.map(agent -> new DeliveryAgentDto(agent.getDeliveryAgentId(), agent.getStatus(),
					agent.getAgentSequence(), routeMap.get(agent.getAgentSequence())))
				.collect(Collectors.toList());

			// 4. 응답 조합
			return new DeliveryDetailsResponse(deliveryId, agentsWithRoutes);

		} catch (CustomException e) {
			log.warn("배달 상세 조회 중 사용자 정의 예외 발생: message={}", e.getMessage());
			throw e; // 이미 정의된 예외는 그대로 던짐
		} catch (Exception e) {
			log.error("배달 상세 조회 중 오류 발생: deliveryId={}, 오류: {}", deliveryId, e.getMessage(), e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
