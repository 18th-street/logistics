package com.eighteenthstreet.deliveryrouteservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryCreatedEvent {
    private UUID startHubId;
    private UUID endHubId;
    private UUID deliveryId;
}
