package com.eighteenthstreet.hub_route_service.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.hub_route_service.domain.HubRouteRepository;
import com.eighteenthstreet.hub_service.domain.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubRouteService {

	private final HubRepository hubRepository;
	private final HubRouteRepository hubRouteRepository;
	private final RestTemplate restTemplate;
}
