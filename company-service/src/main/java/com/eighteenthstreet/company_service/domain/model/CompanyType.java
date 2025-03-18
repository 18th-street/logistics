package com.eighteenthstreet.company_service.domain.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
	RECEIVER("수령 업체"),
	SUPPLIER("생산 업체"),
	NONE("타입 오류");

	private final String name;

	public static CompanyType from(String type) {
		System.out.println("type = " + type);
		return Arrays.stream(values()).filter(t -> t.name.equals(type))
			.findFirst()
			.orElse(NONE);
	}
}
