package com.eighteenthstreet.deliveryrouteservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;
import com.eighteenthstreet.deliveryrouteservice.domain.repository.DeliveryRouteRepository;

public interface JpaDeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>, DeliveryRouteRepository {
}
