package com.eighteenthstreet.company_service.application.dto;

import java.util.UUID;

import com.eighteenthstreet.company_service.domain.model.Company;

public record CreateCompanyResponse(UUID companyId) {

	public static CreateCompanyResponse from(Company company) {
		return new CreateCompanyResponse(company.getId());
	}
}
