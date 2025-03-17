package com.eighteenthstreet.hub_service.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.eighteenthstreet.hub_service.presentation.request.CreateHubRequest;
import com.eighteenthstreet.hub_service.presentation.request.UpdateHubRequest;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_hub")
public class Hub extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(precision = 10, scale = 8, nullable = false)
	private BigDecimal latitude;

	@Column(precision = 11, scale = 8, nullable = false)
	private BigDecimal longitude;

	public static Hub create(CreateHubRequest request) {
		return Hub.builder()
			.name(request.getName())
			.address(request.getAddress())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.build();
	}

	public void update(UpdateHubRequest request) {
		this.setName(request.getName());
		this.setAddress(request.getAddress());
		this.setLatitude(request.getLatitude());
		this.setLongitude(request.getLongitude());
	}
}
