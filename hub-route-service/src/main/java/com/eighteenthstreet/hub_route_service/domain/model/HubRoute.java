package com.eighteenthstreet.hub_route_service.domain.model;

import java.util.UUID;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HubRoute extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubRouteId;

	@Column(name = "estimated_distance")
	private double estimatedDistance;

	@Column(name = "estimated_duration")
	private double estimatedDuration;

	@Column
	private String status;

	@Column(name = "departure_hub_id")
	private UUID departureHubId;

	@Column(name = "arrival_hub_id")
	private UUID arrivalHubId;
}
