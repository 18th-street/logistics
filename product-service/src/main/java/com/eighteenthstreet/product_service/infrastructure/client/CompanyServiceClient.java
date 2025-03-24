package com.eighteenthstreet.product_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eighteenthstreet.product_service.application.dto.SelectCompanyResponse;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {
	@GetMapping("/api/v1/companies/{companyId}")
	SelectCompanyResponse getCompany(@PathVariable UUID companyId);
}
