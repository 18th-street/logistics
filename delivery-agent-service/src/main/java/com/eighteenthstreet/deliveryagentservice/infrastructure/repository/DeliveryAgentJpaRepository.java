package com.eighteenthstreet.deliveryagentservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;

@Repository
public interface DeliveryAgentJpaRepository extends JpaRepository<DeliveryAgent, UUID>, DeliveryAgentRepository {

}
