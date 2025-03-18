package com.eigtheenthstreet.order_service.presentation.request;

import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchConditionSort {
	CREATED_AT(Sort.by(Sort.Direction.DESC, "createdAt")),
	MODIFIED_AT(Sort.by(Sort.Direction.DESC, "modifiedAt"));

	private final Sort sort;

	public static SearchConditionSort of(String value) {
		for (SearchConditionSort conditionSort : values()) {
			if (conditionSort.name().equalsIgnoreCase(value)) {
				return conditionSort;
			}
		}
		return CREATED_AT;
	}
}
