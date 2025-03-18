package com.eighteenthstreet.company_service.presentation.request;

import java.util.UUID;

public record UpdateCompanyRequest(
	UUID hubId,
	String name,
	String type,
	String address
) {
}
