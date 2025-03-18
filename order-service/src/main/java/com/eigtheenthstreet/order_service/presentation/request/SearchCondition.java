package com.eigtheenthstreet.order_service.presentation.request;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchCondition {
	private static final int DEFAULT_PAGE = 0;

	private static final int DEFAULT_SIZE = 10;

	private static final Set<Integer> AVAILABLE_SIZES = Set.of(10, 30, 50);

	private final int page;

	private final int size;

	private final SearchConditionSort sort;

	private final String query;

	public static SearchCondition of(String requestPage, String requestSize, String requestSort, String query) {
		int page = parseIntPageOrDefault(requestPage);
		int size = parseIntSizeOrDefault(requestSize);
		SearchConditionSort sort = SearchConditionSort.of(requestSort);
		return new SearchCondition(page, size, sort, query);
	}

	private static int parseIntPageOrDefault(String requestPage) {
		try {
			return Integer.parseInt(requestPage);
		} catch (NumberFormatException e) {
			return DEFAULT_PAGE;
		}
	}

	private static int parseIntSizeOrDefault(String requestSize) {
		try {
			int size = Integer.parseInt(requestSize);
			return AVAILABLE_SIZES.contains(size) ? size : DEFAULT_SIZE;
		} catch (NumberFormatException e) {
			return DEFAULT_SIZE;
		}
	}
}
