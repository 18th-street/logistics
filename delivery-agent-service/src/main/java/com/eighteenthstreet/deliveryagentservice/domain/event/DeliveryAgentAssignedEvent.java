package com.eighteenthstreet.deliveryagentservice.domain.event;

import java.util.UUID;

public record DeliveryAgentAssignedEvent(UUID deliveryId, UUID deliveryAgentId) {
}