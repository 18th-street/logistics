package com.eighteenthstreet.hub_route_service.domain;

import java.util.List;
import java.util.Optional;

import com.eighteenthstreet.hub_route_service.domain.model.HubRoute;
import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface HubRouteRepository {
	Optional<List<HubRoute>> findByDepartureHubIdAndArrivalHubId(Hub departureHub, Hub arrivalHub);
	Optional<HubRoute> findByDepartureHubIdAndArrivalHubId(Hub departureHub, Hub arrivalHub);

	List<HubRoute> findAllByDepartureHubId(Hub startHub);
}
