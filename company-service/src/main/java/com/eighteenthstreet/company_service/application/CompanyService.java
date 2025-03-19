package com.eighteenthstreet.company_service.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.company_service.application.dto.CreateCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.GetHubResponse;
import com.eighteenthstreet.company_service.application.dto.SelectCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.UpdateCompanyResponse;
import com.eighteenthstreet.company_service.domain.model.Company;
import com.eighteenthstreet.company_service.domain.repository.CompanyRepository;
import com.eighteenthstreet.company_service.exception.CustomCompanyAlreadyExistException;
import com.eighteenthstreet.company_service.exception.CustomCompanyHubNotFoundException;
import com.eighteenthstreet.company_service.exception.CustomCompanyNotFoundException;
import com.eighteenthstreet.company_service.infrastructure.client.HubServiceClient;
import com.eighteenthstreet.company_service.presentation.request.CreateCompanyRequest;
import com.eighteenthstreet.company_service.presentation.request.UpdateCompanyRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {
	private final CompanyRepository companyRepository;
	private final HubServiceClient hubServiceClient;
	//private final ProductServiceClient productServiceClient;

	@Transactional
	public CreateCompanyResponse registerCompany(CreateCompanyRequest request, UUID userId) {
		// 허브 검사
		try {
			GetHubResponse hubResponse = hubServiceClient.getHub(request.hubId());
		} catch (Exception e) {
			log.error("{업체 등록 시 Hub 조회 API 호출 실패: {}", e.getMessage());
			throw new CustomCompanyHubNotFoundException(ErrorCode.COMPANY_HUB_NOT_FOUND);
		}

		if (companyRepository.existsByName(request.name())) {
			throw new CustomCompanyAlreadyExistException(ErrorCode.COMPANY_ALREADY_EXIST);
		}

		Company company = Company.create(request, userId);
		companyRepository.save(company);

		return CreateCompanyResponse.from(company);
	}

	@Transactional
	public UpdateCompanyResponse updateCompany(
		UUID companyId,
		UpdateCompanyRequest request
	) {
		// 허브 검사
		try {
			GetHubResponse hubResponse = hubServiceClient.getHub(request.hubId());
		} catch (Exception e) {
			log.error("{업체 등록 시 Hub 조회 API 호출 실패: {}", e.getMessage());
			throw new CustomCompanyHubNotFoundException(ErrorCode.COMPANY_HUB_NOT_FOUND);
		}

		// company 조회
		Company foundCompany = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomCompanyNotFoundException(ErrorCode.COMPANY_NOT_FOUND));

		foundCompany.update(request);

		// 응답 반환
		return UpdateCompanyResponse.from(foundCompany);
	}

	@Transactional
	public void deleteCompany(UUID companyId) {
		// company 조회
		Company foundCompany = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomCompanyNotFoundException(ErrorCode.COMPANY_NOT_FOUND));

		// company soft delete
		foundCompany.performSoftDelete();

		// todo. 업체에 등록된 상품 삭제
	}

	@Transactional(readOnly = true)
	public SelectCompanyResponse getCompany(UUID companyId) {
		// company 조회
		Company foundCompany = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomCompanyNotFoundException(ErrorCode.COMPANY_NOT_FOUND));
		return SelectCompanyResponse.from(foundCompany);
	}

	@Transactional(readOnly = true)
	public PagedModel<SelectCompanyResponse> getCompanies(String query, Pageable pageable) {
		Page<Company> companies = companyRepository.searchByCompanies(query, pageable);
		Page<SelectCompanyResponse> content = companies.map(SelectCompanyResponse::from);
		return new PagedModel<>(content);
	}
}
