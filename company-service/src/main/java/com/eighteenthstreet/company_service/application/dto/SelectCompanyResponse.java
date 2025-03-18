package com.eighteenthstreet.company_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.company_service.domain.model.Company;

public record SelectCompanyResponse(
	UUID companyId,
	UUID hubId,
	String name,
	String type,
	String address
) {
	public static SelectCompanyResponse from(Company foundCompany) {
		return new SelectCompanyResponse(
			foundCompany.getId(),
			foundCompany.getHubId(),
			foundCompany.getName(),
			foundCompany.getType().getName(),
			foundCompany.getAddress()
		);
	}
}
