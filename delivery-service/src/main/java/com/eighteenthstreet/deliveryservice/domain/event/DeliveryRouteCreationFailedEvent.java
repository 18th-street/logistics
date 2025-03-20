package com.eighteenthstreet.deliveryservice.domain.event;

import java.util.UUID;

import exception.ErrorCode;

public record DeliveryRouteCreationFailedEvent(UUID deliveryId, ErrorCode errorCode) {
}