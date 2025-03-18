package com.eighteenthstreet.company_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.company_service.domain.model.Company;
import com.eighteenthstreet.company_service.domain.repository.CompanyRepository;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID>, CompanyRepository {
	Optional<Company> findById(UUID companyId);

	boolean existsByName(String name);

	@Query("select c from Company c "
		+ "where (:query is null or c.name like %:query%)")
	Page<Company> searchByCompanies(String query, Pageable pageable);
}

