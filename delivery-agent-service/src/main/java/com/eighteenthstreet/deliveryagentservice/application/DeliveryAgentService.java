package com.eighteenthstreet.deliveryagentservice.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.application.dto.DeliveryAgentDto;
import com.eighteenthstreet.deliveryagentservice.application.dto.GetDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.domain.exception.DeliveryAgentNotFoundException;
import com.eighteenthstreet.deliveryagentservice.domain.exception.InvalidDeliveryAgentException;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;
import com.eighteenthstreet.deliveryagentservice.presentation.exception.error.CustomException;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import com.eighteenthstreet.deliveryagentservice.presentation.request.UpdateDeliveryTypeRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAgentService {

	private final DeliveryAgentRepository deliveryAgentRepository;

	public CreateDeliveryAgentResponse createDeliveryAgent(CreateDeliveryAgentRequest request, UUID userId) {

		// TODO: 존재하는 허브인지 확인

		DeliveryAgent deliveryAgent = DeliveryAgent.builder()
			.hubId(request.getHubId())
			.userId(userId)
			.deliveryAgentType(request.getDeliveryAgentType())
			.deliveryAgentStatus(DeliveryAgentStatus.AVAILABLE)
			.slackId(request.getSlackId())
			.build();

		return CreateDeliveryAgentResponse.fromEntity(deliveryAgentRepository.save(deliveryAgent));
	}

	public GetDeliveryAgentResponse getDeliveryAgent(UUID id) {
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(id)
			.orElseThrow(() -> new DeliveryAgentNotFoundException(ErrorCode.DELIVERY_AGENT_NOT_FOUND));

		return GetDeliveryAgentResponse.fromEntity(deliveryAgent);
	}

	@Transactional
	public void updateDeliveryAgentType(UpdateDeliveryTypeRequest request) {
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(request.getDeliveryAgentId())
			.orElseThrow(() -> new DeliveryAgentNotFoundException(ErrorCode.DELIVERY_AGENT_NOT_FOUND));

		deliveryAgent.updateDeliveryAgentType(request.getDeliveryAgentType());
	}

	@Transactional
	public void deleteDeliveryAgent(UUID id) {
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(id)
			.orElseThrow(() -> new DeliveryAgentNotFoundException(ErrorCode.DELIVERY_AGENT_NOT_FOUND));

		if (deliveryAgent.getDeliveryAgentStatus() == DeliveryAgentStatus.IN_DELIVERY) {
			throw new InvalidDeliveryAgentException(ErrorCode.INVALID_DELIVERY_AGENT_STATUS);
		}

		deliveryAgent.softDelete();
	}

	// Feign 호출용 새 메서드
	@Transactional
	public void deleteDeliveryAgentByDeliveryId(UUID deliveryId) {
		List<DeliveryAgent> deliveryAgents = deliveryAgentRepository.findByDeliveryId(deliveryId);

		if (deliveryAgents.isEmpty()) {
			throw new CustomException(ErrorCode.DELIVERY_AGENT_NOT_FOUND);
		}

		for (DeliveryAgent deliveryAgent : deliveryAgents) {
			if (deliveryAgent.getDeliveryAgentStatus() == DeliveryAgentStatus.IN_DELIVERY) {
				throw new CustomException(ErrorCode.INVALID_DELIVERY_AGENT_STATUS);
			}
			deliveryAgent.deleteDeliveryAgent(DeliveryAgentStatus.AVAILABLE);
		}
	}

	@Transactional(readOnly = true)
	public List<DeliveryAgentDto> getDeliveryAgentsByDeliveryId(UUID deliveryId) {
		List<DeliveryAgent> agents = deliveryAgentRepository.findByDeliveryId(deliveryId);

		return agents.stream()
			.map(agent -> new DeliveryAgentDto(agent.getDeliveryAgentId(), agent.getDeliveryAgentStatus().name(),
				agent.getSequence(), null))
			.toList();
	}
}
