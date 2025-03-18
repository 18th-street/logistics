package com.eighteenthstreet.hub_route_service.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.hub_route_service.domain.HubRouteRepository;
import com.eighteenthstreet.hub_route_service.domain.model.HubRoute;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID>, HubRouteRepository {

}
