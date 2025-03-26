package com.eighteenthstreet.deliveryagentservice.domain.event;

import java.util.UUID;

import exception.ErrorCode;

public record DeliveryFailedEvent(UUID deliveryId, ErrorCode errorCode) {
}