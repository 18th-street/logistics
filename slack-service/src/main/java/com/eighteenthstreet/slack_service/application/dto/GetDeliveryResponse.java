package com.eighteenthstreet.slack_service.application.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public record GetDeliveryResponse(
	UUID deliveryId,
	UUID startHubId,
	UUID endHubId,
	List<GetDeliveryRouteResponse> routes,
	String destinationAddress
) {
	public record GetDeliveryRouteResponse(
		Integer sequence,
		UUID startHubId,
		UUID endHubId
	) {
	}

	// 🚀 정렬된 허브 경로를 반환하는 메서드 (인스턴스 메서드)
	public List<UUID> getSortedDeliveryRoute() {
		// `sequence` 기준으로 정렬 (오름차순)
		List<GetDeliveryRouteResponse> sortedRoutes = new ArrayList<>(routes);
		sortedRoutes.sort(Comparator.comparing(GetDeliveryRouteResponse::sequence));

		// 허브 ID를 순서대로 저장할 LinkedHashSet (중복 제거 & 순서 유지)
		LinkedHashSet<UUID> sortedHubs = new LinkedHashSet<>();

		// 시작 허브 추가
		sortedHubs.add(startHubId);

		// 경유 허브들 추가
		for (GetDeliveryRouteResponse route : sortedRoutes) {
			sortedHubs.add(route.startHubId());
			sortedHubs.add(route.endHubId());
		}

		// 마지막 허브 추가 (endHub)
		sortedHubs.add(endHubId);

		// 리스트로 변환하여 반환
		return new ArrayList<>(sortedHubs);
	}
}
