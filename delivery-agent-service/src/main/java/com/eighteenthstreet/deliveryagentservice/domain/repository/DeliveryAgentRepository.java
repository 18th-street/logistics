package com.eighteenthstreet.deliveryagentservice.domain.repository;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAgentRepository {
    DeliveryAgent save(DeliveryAgent deliveryAgent);

    Optional<DeliveryAgent> findById(UUID id);
}
