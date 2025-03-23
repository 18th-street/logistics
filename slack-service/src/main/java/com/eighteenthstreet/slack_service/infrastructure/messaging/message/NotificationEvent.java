package com.eighteenthstreet.slack_service.infrastructure.messaging.message;

import java.util.UUID;

public record NotificationEvent(UUID orderId) {
	public static NotificationEvent from(UUID orderId) {
		return new NotificationEvent(orderId);
	}
}