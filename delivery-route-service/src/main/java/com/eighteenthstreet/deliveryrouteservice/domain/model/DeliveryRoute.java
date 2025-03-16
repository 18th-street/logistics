package com.eighteenthstreet.deliveryrouteservice.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_route")
public class DeliveryRoute {
    @Id
    @UuidGenerator
    private UUID deliveryRouteId;

    @Column(name = "delivery_id")
    private UUID deliveryId; // 배송 ID (외래키로 Delivery와 연결)

    @Column(name = "sequence")
    private int sequence; // 경로 순서

    @Column(name = "start_hub_id")
    private UUID startHubId; // 시작 허브 ID

    @Column(name = "end_hub_id")
    private UUID endHubId; // 종료 허브 ID

    @Column(name = "estimated_distance")
    private String estimatedDistance; // 예상 거리 (예: "50km")

    @Column(name = "estimated_time")
    private String estimatedTime; // 예상 소요 시간 (예: "2h 30m")

    @Column(name = "actual_distance")
    private String actualDistance; // 실제 거리 (추후 업데이트 가능)

    @Column(name = "actual_time")
    private String actualTime; // 실제 소요 시간 (추후 업데이트 가능)
}
