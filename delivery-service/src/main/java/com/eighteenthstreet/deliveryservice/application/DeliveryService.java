package com.eighteenthstreet.deliveryservice.application;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryAgentDto;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryDetailsResponse;
import com.eighteenthstreet.deliveryservice.application.dto.DeliveryRouteDto;
import com.eighteenthstreet.deliveryservice.application.event.DeliveryEventPublisher;
import com.eighteenthstreet.deliveryservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.infrastructure.client.DeliveryAgentClient;
import com.eighteenthstreet.deliveryservice.infrastructure.client.DeliveryRouteClient;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryCancelledErrMessage;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryCancelledEvent;
import com.eighteenthstreet.deliveryservice.infrastructure.messaging.message.DeliveryMessage;
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

	private final DeliveryEventPublisher deliveryEventPublisher;
	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteClient deliveryRouteClient;
	private final DeliveryAgentClient deliveryAgentClient;

	// 배달생성
	public CreateDeliveryResponse createDelivery(CreateDeliveryRequest createDeliveryRequest) {
		Delivery delivery = Delivery.createDelivery(createDeliveryRequest);

		delivery = deliveryRepository.save(delivery);

		DeliveryCreatedEvent event = new DeliveryCreatedEvent(createDeliveryRequest.getStartHubId(),
			createDeliveryRequest.getEndHubId(), delivery.getDeliveryId(), UUID.randomUUID());
		deliveryEventPublisher.publishDeliveryCreated(event);

		return CreateDeliveryResponse.fromEntity(delivery);
	}

	// 메세지 처리하는 서비스
	public void createMessageDelivery(DeliveryMessage message) {
		Delivery delivery = Delivery.createMessageDelivery(message);

		delivery = deliveryRepository.save(delivery);

		DeliveryCreatedEvent event = new DeliveryCreatedEvent(message.startHubId(), message.endHubId(),
			delivery.getDeliveryId(), message.orderId());

		deliveryEventPublisher.publishDeliveryCreated(event);
		CreateDeliveryResponse.fromEntity(delivery);
	}

	@Transactional
	public void updateDeliveryStatus(UpdateStatusDeliveryRequest request) {
		Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
			.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.updateStatus(request.getDeliveryStatus());
		deliveryRepository.save(delivery);
	}

	@Transactional
	public void deleteDelivery(UUID deliveryId) {

		// 1. Delivery 조회 및 삭제
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.delete();
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
			Delivery delivery = deliveryRepository.findById(deliveryId)
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
			return new DeliveryDetailsResponse(deliveryId, delivery.getDestinationAddress(), agentsWithRoutes);

		} catch (CustomException e) {
			log.warn("배달 상세 조회 중 사용자 정의 예외 발생: message={}", e.getMessage());
			throw e; // 이미 정의된 예외는 그대로 던짐
		} catch (Exception e) {
			log.error("배달 상세 조회 중 오류 발생: deliveryId={}, 오류: {}", deliveryId, e.getMessage(), e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public void cancelledDelivery(DeliveryCancelledEvent message) {
		// Delivery 존재 여부 확인
		Delivery delivery = deliveryRepository.findById(message.deliveryId())
			.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.cancel();

		try {
			// 2. DeliveryRoute 삭제 (Feign Client 호출)
			ResponseEntity<Map<String, String>> routeResponse = deliveryRouteClient.deleteDeliveryRouteByDeliveryId(
				message.deliveryId());
			if (!routeResponse.getStatusCode().is2xxSuccessful()) {
				throw new CustomException(ErrorCode.DELIVERY_ROUTE_DELETION_FAILED);
			}

			// 3. DeliveryAgent 삭제 (Feign Client 호출)
			ResponseEntity<Map<String, String>> agentResponse = deliveryAgentClient.deleteDeliveryAgentByDeliveryId(
				message.deliveryId());
			if (!agentResponse.getStatusCode().is2xxSuccessful()) {
				throw new CustomException(ErrorCode.DELIVERY_AGENT_DELETION_FAILED);
			}

		} catch (Exception e) {
			// 배달 삭세 실패하면 메세지 보내기
			DeliveryCancelledErrMessage cancelledErrMessage = new DeliveryCancelledErrMessage(message.orderId(),
				e.getMessage());
			deliveryEventPublisher.publishDeliveryCancelledError(cancelledErrMessage);
			log.info(e.getMessage());
			throw e;
		}

		// 배달 삭제 성공하면 메세지 보내기
		deliveryEventPublisher.publishDeliveryCancelled(message);
	}
}
