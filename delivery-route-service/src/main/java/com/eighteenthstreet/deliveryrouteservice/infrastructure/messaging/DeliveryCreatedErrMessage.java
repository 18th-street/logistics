package com.eighteenthstreet.deliveryrouteservice.infrastructure.messaging;

import java.util.UUID;

public record DeliveryCreatedErrMessage(UUID orderId) {
}