package com.eighteenthstreet.deliveryrouteservice.domain.model;


import base.BaseEntity;
import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryCreatedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

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

    @Column(name = "estimated_time")
    private Integer estimatedTime; // 예상 소요 시간

    @Column(name = "actual_distance")
    private Double actualDistance; // 실제 거리 (추후 업데이트 가능)

    @Column(name = "actual_time")
    private Integer actualTime; // 실제 소요 시간 (추후 업데이트 가능)

    public static DeliveryRoute createDeliveryRoute(DeliveryCreatedEvent deliveryCreatedEvent) {
        // TODO: 허브 간 조회를 하고 추가 할 에정
        return DeliveryRoute.builder()
                .deliveryId(deliveryCreatedEvent.getDeliveryId())
                .startHubId(deliveryCreatedEvent.getStartHubId())
                .endHubId(deliveryCreatedEvent.getEndHubId())
                .build();

    }
}
