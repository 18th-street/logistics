package com.eighteenthstreet.deliveryrouteservice.domain.repository;


import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {
    void save(DeliveryRoute deliveryRoute);

    Optional<DeliveryRoute> findById(UUID deliveryAgentId);
}
