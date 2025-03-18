package com.eighteenthstreet.hub_route_service.application;

import org.springframework.stereotype.Service;

import com.eighteenthstreet.hub_route_service.domain.HubRouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubRouteService {

	private final HubRouteRepository hubRouteRepository;
}
