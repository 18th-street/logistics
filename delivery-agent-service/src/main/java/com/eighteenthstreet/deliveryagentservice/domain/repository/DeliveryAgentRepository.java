package com.eighteenthstreet.deliveryagentservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;

public interface DeliveryAgentRepository {
	DeliveryAgent save(DeliveryAgent deliveryAgent);

	Optional<DeliveryAgent> findById(UUID id);

	List<DeliveryAgent> findByDeliveryAgentStatus(DeliveryAgentStatus status);
}
