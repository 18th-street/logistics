package com.eighteenthstreet.deliveryagentservice.infrastructure.repository;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryAgentJpaRepository extends JpaRepository<DeliveryAgent, UUID>, DeliveryAgentRepository {

}
