package com.eighteenthstreet.deliveryagentservice.domain.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "p_delivery_agent")
public class DeliveryAgent extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "delivery_agent_id")
    private UUID deliveryAgentId;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "delivery_route_id")
    private UUID deliveryRouteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeliveryAgentType deliveryAgentType;


    // 추후 GPS 등 이용해서 추적을 위한 필드
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    private Integer sequence;

    @Column(name = "slack_id")
    private String slackId;

}
