package com.eighteenthstreet.product_service.application.dto;

import java.util.UUID;

public record SelectCompanyResponse(
	UUID companyId,
	UUID hubId,
	String name,
	String type,
	String address
) {
}
