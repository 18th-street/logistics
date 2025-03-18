package com.eighteenthstreet.company_service.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.company_service.application.dto.CreateCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.SelectCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.UpdateCompanyResponse;
import com.eighteenthstreet.company_service.domain.model.Company;
import com.eighteenthstreet.company_service.domain.repository.CompanyRepository;
import com.eighteenthstreet.company_service.exception.CustomCompanyAlreadyExistException;
import com.eighteenthstreet.company_service.exception.CustomCompanyNotFoundException;
import com.eighteenthstreet.company_service.presentation.request.CreateCompanyRequest;
import com.eighteenthstreet.company_service.presentation.request.UpdateCompanyRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
	private final CompanyRepository companyRepository;
	// todo
	// private final HubServiceClient hubServiceClient;
	// private final ProductServiceClient productServiceClient;

	@Transactional
	public CreateCompanyResponse registerCompany(CreateCompanyRequest request) {
		// todo. 허브 검사
		// hubService.findHub(request.hubId());

		if (companyRepository.existsByName(request.name())) {
			throw new CustomCompanyAlreadyExistException(ErrorCode.COMPANY_ALREADY_EXIST);
		}

		Company company = Company.create(request);
		companyRepository.save(company);

		return CreateCompanyResponse.from(company);
	}

	@Transactional
	public UpdateCompanyResponse updateCompany(
		UUID companyId,
		UpdateCompanyRequest request
		//Long userId,
		//String role
	) {
		// todo. hub 검사
		// hubService.findHub(request.hubId());

		// company 조회
		Company foundCompany = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomCompanyNotFoundException(ErrorCode.COMPANY_NOT_FOUND));

		// todo. 업체 담당자만 수정 가능
		//if (role.equals("COMPANY_MANAGER") && !Objects.equals(foundCompany.getManagerId(), userId)) {}
		// 내용 수정
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
