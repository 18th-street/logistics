package com.eighteenthstreet.deliveryagentservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@SQLRestriction("deleted_at IS NULL")
@Table(name = "p_delivery_agent",
	uniqueConstraints = {@UniqueConstraint(columnNames = "user_id")})
public class DeliveryAgent extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "delivery_agent_id")
	private UUID deliveryAgentId;

	@Column(name = "hub_id")
	private UUID hubId;

	@Column(name = "user_id", unique = true)
	private UUID userId;

	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(name = "delivery_route_id")
	private UUID deliveryRouteId;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private DeliveryAgentType deliveryAgentType;

	// 배달 담당자가 배달 가능하지 확인하기 위한 타입
	// 초기엔 배달가능한 상태로 설정
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private DeliveryAgentStatus deliveryAgentStatus;

	// 추후 GPS 등 이용해서 추적을 위한 필드
	@Column(name = "assigned_at")
	private LocalDateTime assignedAt;

	private Integer sequence;

	@Column(name = "slack_id")
	private String slackId;

	public void updateDeliveryAgentType(DeliveryAgentType deliveryAgentStatus) {
		this.deliveryAgentType = deliveryAgentStatus;
	}

	public void deleteDeliveryAgent(DeliveryAgentStatus status) {
		this.deliveryAgentStatus = status;
		this.deliveryId = null;    // 기존 deliveryId 지우기
		this.sequence = 0;         // 기존 sequence 지우기 (초기화)
	}
}
