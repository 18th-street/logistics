package com.eighteenthstreet.deliveryagentservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;

public interface DeliveryAgentRepository {
	DeliveryAgent save(DeliveryAgent deliveryAgent);

	Optional<DeliveryAgent> findById(UUID id);

	List<DeliveryAgent> findByDeliveryAgentStatus(DeliveryAgentStatus status);

	List<DeliveryAgent> findByDeliveryId(UUID deliveryId);

	List<DeliveryAgent> findByDeliveryAgentStatusAndHubIdAndDeliveryAgentType(DeliveryAgentStatus deliveryAgentStatus,
		UUID hubId, DeliveryAgentType deliveryAgentType);

	List<DeliveryAgent> findByDeliveryAgentStatusAndDeliveryAgentType(DeliveryAgentStatus deliveryAgentStatus,
		DeliveryAgentType deliveryAgentType);
}
