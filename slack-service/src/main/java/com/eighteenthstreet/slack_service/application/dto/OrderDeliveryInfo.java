package com.eighteenthstreet.slack_service.application.dto;

import java.util.List;
import java.util.UUID;

public record OrderDeliveryInfo(
	UUID orderId,
	List<String> productName,
	List<Integer> productQuantity,
	String requests,
	String start,
	List<String> stopping,
	String destination
) {
}
