package com.eigtheenthstreet.order_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtil {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

	public static LocalDateTime parseDateTime(String dateTimeString) {
		if (dateTimeString == null) {
			throw new IllegalArgumentException("dateTimeString cannot be null");
		}

		try {
			return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
		} catch (DateTimeParseException e) {
			log.error(dateTimeString + " is not a valid date");
			throw new DateTimeParseException("Invalid date/time format", dateTimeString, e.getErrorIndex());
		}
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			throw new IllegalArgumentException("dateTime cannot be null");
		}

		return dateTime.format(DATE_TIME_FORMATTER);
	}
}
