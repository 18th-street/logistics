package com.eighteenthstreet.company_service.presentation;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.company_service.application.CompanyService;
import com.eighteenthstreet.company_service.application.dto.CreateCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.SelectCompanyResponse;
import com.eighteenthstreet.company_service.application.dto.UpdateCompanyResponse;
import com.eighteenthstreet.company_service.presentation.request.CreateCompanyRequest;
import com.eighteenthstreet.company_service.presentation.request.SearchCondition;
import com.eighteenthstreet.company_service.presentation.request.UpdateCompanyRequest;

import auth.CheckRole;
import auth.JwtUtil;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {
	private final JwtUtil jwtUtil;
	private final CompanyService companyService;

	@Operation(summary = "업체 등록", description = "마스터 관리자, 허브 관리자는 업체 등록을 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@PostMapping()
	public ResponseEntity<CreateCompanyResponse> registerCompany(
		@RequestBody CreateCompanyRequest request,
		@RequestHeader("Authorization") String token
	) {
		UUID userId = jwtUtil.getUserIdFromToken(token);

		CreateCompanyResponse response = companyService.registerCompany(request, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "업체 수정", description = "마스터 관리자, 허브 관리자, 업체 관리자는 업체 수정을 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB, Role.COMPANY})
	@PatchMapping("/{companyId}")
	public ResponseEntity<UpdateCompanyResponse> updateCompany(
		@PathVariable("companyId") UUID companyId,
		@RequestBody UpdateCompanyRequest request
	) {
		UpdateCompanyResponse response = companyService.updateCompany(companyId, request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "업체 삭제", description = "마스터 관리자, 허브 관리자는 업체 삭제를 할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@DeleteMapping("/{companyId}")
	public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
		companyService.deleteCompany(companyId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "업체 조회", description = "모든 사용자는 업체 조회를 할 수 있습니다.")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{companyId}")
	public ResponseEntity<SelectCompanyResponse> getCompany(@PathVariable UUID companyId) {
		SelectCompanyResponse response = companyService.getCompany(companyId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "업체 검색", description = "모든 사용자는 업체 검색을 할 수 있습니다.")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping()
	public ResponseEntity<PagedModel<SelectCompanyResponse>> getAllCompanies(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(name = "q", required = false) String query
	) {
		SearchCondition searchCondition = SearchCondition.of(
			String.valueOf(page),
			String.valueOf(size),
			sort,
			query
		);

		PageRequest pageable = PageRequest.of(
			searchCondition.getPage(),
			searchCondition.getSize(),
			searchCondition.getSort().getSort()
		);

		PagedModel<SelectCompanyResponse> response = companyService.getCompanies(query, pageable);
		return ResponseEntity.ok(response);
	}
}
