package com.eigtheenthstreet.order_service.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eigtheenthstreet.order_service.infrastructure.client.dto.CreateCompanyResponse;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {
	@GetMapping("/api/v1/companies/{companyId}")
	CreateCompanyResponse getCompany(@PathVariable UUID companyId);
}
