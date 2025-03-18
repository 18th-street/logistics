package com.eighteenthstreet.deliveryservice.application.dto;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetDeliveryResponse {
    private UUID deliveryId;
    private UUID orderId;
    private UUID startHubId;
    private UUID endHubId;
    private DeliveryStatus status;

    public static GetDeliveryResponse fromEntity(Delivery delivery) {
        return GetDeliveryResponse.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .startHubId(delivery.getStartHubId())
                .endHubId(delivery.getEndHubId())
                .status(delivery.getStatus())
                .build();
    }
}
