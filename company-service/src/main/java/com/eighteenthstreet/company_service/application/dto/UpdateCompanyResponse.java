package com.eighteenthstreet.company_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.company_service.domain.model.Company;

public record UpdateCompanyResponse(
	UUID companyId,
	UUID hubId,
	String name,
	String type,
	String address
) {
	public static UpdateCompanyResponse from(Company foundCompany) {
		return new UpdateCompanyResponse(
			foundCompany.getId(),
			foundCompany.getHubId(),
			foundCompany.getName(),
			foundCompany.getType().name(),
			foundCompany.getAddress()
		);
	}
}
