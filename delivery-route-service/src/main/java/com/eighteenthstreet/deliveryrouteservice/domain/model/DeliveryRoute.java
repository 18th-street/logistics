package com.eighteenthstreet.deliveryrouteservice.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryCreatedEvent;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SQLRestriction("deleted_at IS NULL")
@Table(name = "p_delivery_route")
public class DeliveryRoute extends BaseEntity {
	@Id
	@UuidGenerator
	@Column(name = "delivery_route_id")
	private UUID deliveryRouteId;

	@Column(name = "delivery_id")
	private UUID deliveryId; // 배송 ID (외래키로 Delivery와 연결)

	@Column(name = "sequence")
	private Integer sequence; // 경로 순서

	@Column(name = "start_hub_id")
	private UUID startHubId; // 시작 허브 ID

	@Column(name = "end_hub_id")
	private UUID endHubId; // 종료 허브 ID

	@Column(name = "estimated_distance")
	private Double estimatedDistance; // 예상 거리

	@Column(name = "estimated_duration")
	private Double estimatedDuration; // 예상 소요 시간

	@Column(name = "actual_distance")
	private Double actualDistance; // 실제 거리 (추후 업데이트 가능)

	@Column(name = "actual_time")
	private Integer actualDuration; // 실제 소요 시간 (추후 업데이트 가능)

	public static DeliveryRoute createDeliveryRoute(DeliveryCreatedEvent deliveryCreatedEvent) {
		// TODO: 허브 간 조회를 하고 추가 할 에정
		return DeliveryRoute.builder()
			.deliveryId(deliveryCreatedEvent.getDeliveryId())
			.startHubId(deliveryCreatedEvent.getStartHubId())
			.endHubId(deliveryCreatedEvent.getEndHubId())
			.build();

	}
}
