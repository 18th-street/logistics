package com.eighteenthstreet.deliveryagentservice.domain.event;

import java.util.List;
import java.util.UUID;

public record RouteCreatedEvent(UUID deliveryId, List<RouteInfo> routes) {
	public record RouteInfo(int sequence, UUID startHubId, UUID endHubId) {
	}
}
