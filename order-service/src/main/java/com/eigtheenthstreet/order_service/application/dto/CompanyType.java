package com.eigtheenthstreet.order_service.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CompanyType {
	RECEIVER("수령 업체"),
	SUPPLIER("생산 업체");

	private final String name;
}
