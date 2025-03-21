package com.eighteenthstreet.deliveryservice.domain.model;

public enum DeliveryStatus {
	DELIVERY_FAIL, // 배송 실패
	PENDING_AT_HUB,       // 허브 대기중
	IN_TRANSIT_TO_HUB,    // 허브 이동중
	ARRIVED_AT_HUB,       // 목적지 허브 도착
	OUT_FOR_DELIVERY,     // 배송중
	IN_TRANSIT_TO_VENDOR, // 업체 이동중
	CANCELED, DELIVERED;           // 배송완료
}
