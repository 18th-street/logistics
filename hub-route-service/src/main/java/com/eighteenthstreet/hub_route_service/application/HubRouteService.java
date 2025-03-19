package com.eighteenthstreet.hub_route_service.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.hub_route_service.application.dto.GetHubRouteResponse;
import com.eighteenthstreet.hub_route_service.application.dto.NaverMapResponse;
import com.eighteenthstreet.hub_route_service.domain.HubRouteRepository;
import com.eighteenthstreet.hub_route_service.domain.model.HubRoute;
import com.eighteenthstreet.hub_route_service.exception.CustomHubNotFoundException;
import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "허브 라우트 서비스!")
@Service
@RequiredArgsConstructor
public class HubRouteService {

	private final HubRepository hubRepository;
	private final HubRouteRepository hubRouteRepository;
	private final NaverMapService naverMapService;

	public List<GetHubRouteResponse> findOptimalRoute(UUID departureHubId, UUID arrivalHubId) {
		// 출발 허브 & 도착 허브 조회
		Hub departureHub = hubRepository.findById(departureHubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		Hub arrivalHub = hubRepository.findById(arrivalHubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		// BFS 기반 최단 경로 탐색
		List<HubRoute> optimalRoute = bfsFindShortestPath(departureHub, arrivalHub);

		// 결과를 DTO 형태로 변환하여 반환
		return optimalRoute.stream().map(GetHubRouteResponse::from).toList();
	}

		return GetHubRoutesResponse.from(GetHubRouteResponse.fromList(hubRoutes));
	private List<HubRoute> bfsFindShortestPath(Hub startHub, Hub endHub) {
		Queue<List<HubRoute>> queue = new LinkedList<>();
		Set<Hub> visited = new HashSet<>();

		// 출발 허브에서 가능한 모든 경로 탐색 시작
		List<HubRoute> initialRoutes = hubRouteRepository.findAllByDepartureHubId(startHub);
		for (HubRoute route : initialRoutes) {
			queue.offer(Collections.singletonList(route));
		}

		while (!queue.isEmpty()) {
			List<HubRoute> currentPath = queue.poll();
			HubRoute lastRoute = currentPath.get(currentPath.size() - 1);
			Hub lastHub = lastRoute.getArrivalHubId();

			// 목적지 허브에 도착하면 해당 경로 반환
			if (lastHub.equals(endHub)) {
				return currentPath;
			}

			if (!visited.contains(lastHub)) {
				visited.add(lastHub);
				List<HubRoute> nextRoutes = hubRouteRepository.findAllByDepartureHubId(lastHub);

				for (HubRoute nextRoute : nextRoutes) {
					List<HubRoute> newPath = new ArrayList<>(currentPath);
					newPath.add(nextRoute);
					queue.offer(newPath);
				}
			}
		}

		throw new RuntimeException("최적 경로를 찾을 수 없습니다.");
	}
	}
}
