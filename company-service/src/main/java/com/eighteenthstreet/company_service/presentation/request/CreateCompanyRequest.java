package com.eighteenthstreet.company_service.presentation.request;

import java.util.UUID;

public record CreateCompanyRequest(
	UUID hubId,
	String name,
	String type,
	String address
) {
}
