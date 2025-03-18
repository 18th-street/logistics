package com.eighteenthstreet.hub_route_service.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.hub_route_service.application.HubRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
public class HubRouteController {

	private final HubRouteService hubRouteService;
}
