package com.eighteenthstreet.company_service.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.eighteenthstreet.company_service.presentation.request.CreateCompanyRequest;
import com.eighteenthstreet.company_service.presentation.request.UpdateCompanyRequest;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_company")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_company SET is_deleted = true, deleted_at = now() WHERE company_id = ?")
@SQLRestriction("is_deleted = false")
public class Company extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "company_id")
	private UUID id;

	@Column(name = "company_name")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "company_type")
	private CompanyType type;

	@Column(name = "company_address")
	private String address;

	@Column(name = "hub_id")
	private UUID hubId;

	@Column(name = "company_manager_id")
	private Long managerId;

	public static Company create(CreateCompanyRequest request) {
		return Company.builder()
			.name(request.name())
			.type(CompanyType.from(request.type()))
			.address(request.address())
			.hubId(request.hubId())
			//.managerId(userId)
			.build();
	}

	public void update(UpdateCompanyRequest request) {
		this.name = request.name();
		this.type = CompanyType.from(request.type());
		this.address = request.address();
	}

	public void performSoftDelete() {
		this.softDelete();
	}
}
