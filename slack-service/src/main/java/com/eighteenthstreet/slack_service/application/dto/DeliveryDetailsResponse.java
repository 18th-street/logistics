package com.eighteenthstreet.slack_service.application.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public record DeliveryDetailsResponse(
	UUID deliveryId,
	String destinationAddress,
	List<DeliveryAgentDto> deliveryAgents
) {
	public record DeliveryAgentDto(
		UUID deliveryAgentId,
		String status,
		int agentSequence,
		DeliveryRouteDto route
	) {
	}

	public record DeliveryRouteDto(
		UUID routeId,
		int routeSequence,
		UUID startHubId,
		UUID endHubId
	) {
	}

	// 정렬된 허브 경로를 반환하는 메서드 (인스턴스 메서드)
	public List<UUID> getSortedDeliveryRoute() {
		LinkedHashSet<UUID> sortedHubs = new LinkedHashSet<>();

		// deliveryAgent의 route를 routeSequence 순으로 정렬
		List<DeliveryAgentDto> sortedAgents = new ArrayList<>(deliveryAgents);
		sortedAgents.sort(Comparator.comparing(agent -> agent.route().routeSequence()));

		// 정렬된 각 route에서 startHubId, endHubId 추출
		for (DeliveryAgentDto agent : sortedAgents) {
			DeliveryRouteDto route = agent.route();
			sortedHubs.add(route.startHubId());
			sortedHubs.add(route.endHubId());
		}

		return new ArrayList<>(sortedHubs);
	}
}
