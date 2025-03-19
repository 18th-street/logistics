package com.eighteenthstreet.hub_route_service.application;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.hub_route_service.application.dto.NaverMapResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "네이버 API!!")
@Service
@RequiredArgsConstructor
public class NaverMapService {

	private final RestTemplate restTemplate;

	@Value("${naver.api.client-id}")
	private String clientId;

	@Value("${naver.api.client-secret}")
	private String clientSecret;

	private static final String NAVER_MAP_API_URL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";

	public NaverMapResponse getDistanceAndDuration(double startLat, double startLng, double endLat, double endLng) {
		String encodedStart = URLEncoder.encode(String.format("%f,%f", startLng, startLat), StandardCharsets.UTF_8);
		String encodedGoal = URLEncoder.encode(String.format("%f,%f", endLng, endLat), StandardCharsets.UTF_8);

		String url = String.format("%s?goal=%s&start=%s",
			NAVER_MAP_API_URL, encodedGoal, encodedStart);

		log.info("NaverMapService getDistanceAndDuration url: {}", url);

		HttpHeaders headers = new HttpHeaders();
		headers.set("x-ncp-apigw-api-key-id", clientId);
		headers.set("x-ncp-apigw-api-key", clientSecret);
		headers.set("Content-Type", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<NaverMapResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
			NaverMapResponse.class);

		log.info("Response Body: {}", response.getBody().getDistance());
		log.info("Response Body: {}", response.getBody().getDuration());

		return response.getBody();
	}
}
