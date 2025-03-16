package com.eighteenthstreet.deliveryrouteservice.infrastructure.repository;

import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;
import com.eighteenthstreet.deliveryrouteservice.domain.repository.DeliveryRouteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>, DeliveryRouteRepository {
}
