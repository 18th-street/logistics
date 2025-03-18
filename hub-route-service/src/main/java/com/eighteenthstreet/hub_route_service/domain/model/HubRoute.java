package com.eighteenthstreet.hub_route_service.domain.model;

import java.util.UUID;

import com.eighteenthstreet.hub_service.domain.model.Hub;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class HubRoute extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubRouteId;

	@ManyToOne
	@JoinColumn(name = "departure_hub_id", nullable = false)
	private Hub departureHubId;

	@ManyToOne
	@JoinColumn(name = "arrival_hub_id", nullable = false)
	private Hub arrivalHubId;

	@Column(name = "estimated_distance")
	private Double estimatedDistance;

	@Column(name = "estimated_duration")
	private Double estimatedDuration;

	@Column(name = "hop_count") // 경유 허브 개수
	private Integer hopCount;

	@Column
	private String status;
}
