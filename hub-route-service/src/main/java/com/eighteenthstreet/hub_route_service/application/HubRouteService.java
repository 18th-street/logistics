package com.eighteenthstreet.hub_route_service.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.hub_route_service.application.dto.GetHubRouteResponse;
import com.eighteenthstreet.hub_route_service.application.dto.GetHubRoutesResponse;
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
	private final RestTemplate restTemplate;

	private static final Map<String, List<String>> HUB_ROUTES = Map.of(
		"경기 남부 센터", List.of("경기 북부 센터", "서울특별시 센터", "인천광역시 센터", "강원특별자치도 센터"),
		"대전광역시 센터", List.of("충청남도 센터", "충청북도 센터", "세종특별자치시 센터", "전북특별자치도 센터", "광주광역시 센터", "전라남도 센터"),
		"대구광역시 센터", List.of("경상북도 센터", "경상남도 센터", "부산광역시 센터", "울산광역시 센터")
	);

	// @Transactional
	// public void generateHubRoutes() {
	// 	List<Hub> allHubs = hubRepository.findAll();
	// 	Map<String, Hub> hubMap = allHubs.stream().collect(Collectors.toMap(Hub::getName, hub -> hub));
	//
	// 	for (String centralHubName : HUB_ROUTES.keySet()) {
	// 		Hub centralHub = hubMap.get(centralHubName);
	//
	// 		log.info("중앙 허브 : {}", centralHub.getName());
	//
	// 		for (String adjacentHubName : HUB_ROUTES.get(centralHubName)) {
	// 			Hub adjacnetHub = hubMap.get(adjacentHubName);
	//
	// 			log.info("{} -> {}", centralHubName, adjacnetHub.getName());
	// 			if (centralHub != null && adjacnetHub != null) {
	// 				createHubRoute(centralHub, adjacnetHub);
	// 			}
	// 		}
	// 	}
	// }
	//
	// private void createHubRoute(Hub departure, Hub arrival) {
	// 	// Double estimatedDistance = getDistacneFromNaverAPI(departure, arrival);
	// 	// Double estimatedDuration = estimatedDistance / 60;
	//
	// 	HubRoute route = new HubRoute();
	// 	route.setDepartureHubId(departure);
	// 	route.setArrivalHubId(arrival);
	// 	// route.setEstimatedDistance(estimatedDistance);
	// 	// route.setEstimatedDuration(estimatedDuration);
	//
	// 	// HubRoute savedRoute = hubRouteRepository.save(route);
	// }
	//
	// private Double getDistacneFromNaverAPI(Hub departure, Hub arrival) {
	// 	String url = UriComponentsBuilder.fromHttpUrl(NAVER_MAPS_URL)
	// 		.queryParam("start", departure.getLongitude() + "," + departure.getLatitude())
	// 		.queryParam("goal", arrival.getLongitude() + "," + arrival.getLatitude())
	// 		.queryParam("option", "trafast")
	// 		.toUriString();
	//
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("X-NCP-APIGW-API-KEY-ID", NAVER_CLIENT_ID);
	// 	headers.set("X-NCP-APIGW-API-KEY", NAVER_CLIENT_SECRET);
	// 	HttpEntity<String> entity = new HttpEntity<>(headers);
	//
	// 	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	//
	// 	try {
	// 		ObjectMapper mapper = new ObjectMapper();
	// 		JsonNode root = mapper.readTree(response.getBody());
	// 		JsonNode traFastPaths = root.path("route").path("trafast");
	//
	// 		if (traFastPaths.isArray() && traFastPaths.size() > 0) {
	// 			return traFastPaths.get(0).path("summary").path("distance").asDouble() / 1000.0; // 미터 → 킬로미터 변환
	// 		}
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	//
	// 	return 0.0;
	// }

	public GetHubRoutesResponse getHubRoutes(UUID departureHubId, UUID arrivalHubId) {
		Hub departureHub = hubRepository.findById(departureHubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		Hub arrivalHub = hubRepository.findById(arrivalHubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		List<HubRoute> hubRoutes = hubRouteRepository.findByDepartureHubIdAndArrivalHubId(departureHub, arrivalHub)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_ROUTE_NOT_FOUND));

		return GetHubRoutesResponse.from(GetHubRouteResponse.fromList(hubRoutes));
	}
}
