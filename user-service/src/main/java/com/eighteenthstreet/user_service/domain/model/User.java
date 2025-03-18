package com.eighteenthstreet.user_service.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;

import auth.Role;
import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userId;

	@Column(length = 10, nullable = false)
	private String username;

	@Column(length = 300, nullable = false)
	private String password;

	@Column(length = 10, nullable = false)
	private String name;

	@Column(length = 50, nullable = false)
	private String email;

	@Column(length = 30, nullable = false)
	private String phone;

	@Column(length = 30, name = "slack_id")
	private String slackId;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@PrePersist
	public void prePersist() {
		if (userId == null) {
			userId = UUID.randomUUID();
		}
	}

	public void update(UpdateUserRequestDto request) {
		this.email = request.email();
		this.slackId = request.slackId();
		this.name = request.name();
		this.phone = request.phone();
		this.role = request.role();
		this.status = request.status();
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void updateStatus() {
		this.status = Status.COMPLETE;
	}

	public void performSoftDelete() {
		this.softDelete();
	}
}
