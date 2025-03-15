package com.eighteenthstreet.deliveryagentservice.domain.repository;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;

public interface DeliveryAgentRepository {
    DeliveryAgent save(DeliveryAgent deliveryAgent);
}
