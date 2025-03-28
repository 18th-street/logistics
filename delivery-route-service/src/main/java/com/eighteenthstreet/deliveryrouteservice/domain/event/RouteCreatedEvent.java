package com.eighteenthstreet.deliveryrouteservice.domain.event;

import java.util.List;
import java.util.UUID;

public record RouteCreatedEvent(UUID deliveryId, List<RouteInfo> routes) {
	public record RouteInfo(UUID routeId, int sequence, UUID startHubId, UUID endHubId) {
	}
}