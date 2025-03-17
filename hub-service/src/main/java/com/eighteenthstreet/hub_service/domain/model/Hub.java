package com.eighteenthstreet.hub_service.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

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
}
