package com.eighteenthstreet.deliveryrouteservice.domain.event;

import java.util.UUID;

import exception.ErrorCode;

// 실패 이벤트 정의
public record DeliveryRouteCreationFailedEvent(UUID deliveryId, ErrorCode errorCode) {
}
