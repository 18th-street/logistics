package com.eigtheenthstreet.order_service.infrastructure.client.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCompanyResponse(
	UUID companyId,
	String name,
	@JsonProperty("type")
	String companyType,
	UUID hubId
) {
}
