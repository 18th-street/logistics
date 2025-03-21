package com.eighteenthstreet.deliveryrouteservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;

public interface DeliveryRouteRepository {
	DeliveryRoute save(DeliveryRoute deliveryRoute);

	Optional<DeliveryRoute> findById(UUID deliveryAgentId);

	List<DeliveryRoute> findByDeliveryId(UUID deliveryId);
}
