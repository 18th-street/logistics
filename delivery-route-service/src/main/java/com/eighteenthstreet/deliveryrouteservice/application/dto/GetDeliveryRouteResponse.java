package com.eighteenthstreet.deliveryrouteservice.application.dto;

import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetDeliveryRouteResponse {

    private Integer sequence;
    private UUID startHubId;
    private UUID endHubId;
    private Double estimatedDistance;
    private Integer estimatedTime;

    public static GetDeliveryRouteResponse fromEntity(DeliveryRoute deliveryRoute) {
        return GetDeliveryRouteResponse.builder()
                .sequence(deliveryRoute.getSequence())
                .startHubId(deliveryRoute.getStartHubId())
                .endHubId(deliveryRoute.getEndHubId())
                .estimatedDistance(deliveryRoute.getEstimatedDistance())
                .estimatedTime(deliveryRoute.getEstimatedTime())
                .build();
    }


}
