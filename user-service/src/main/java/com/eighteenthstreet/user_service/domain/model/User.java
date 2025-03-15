package com.eighteenthstreet.user_service.domain.model;

import java.time.LocalDateTime;

import com.eighteenthstreet.user_service.presentation.dto.UpdateUserRequestDto;

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
import lombok.ToString;

@ToString
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(length = 10, nullable = false)
	private String username;

	@Column(length = 300, nullable = false)
	private String password;

	@Column(length = 30, nullable = false)
	private String slackId;

	@Column(length = 10, nullable = false)
	private String name;

	@Column(length = 30, nullable = false)
	private String phone;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "deleted_by")
	private String deletedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public void update(UpdateUserRequestDto request) {
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

	public void delete(User loginUser) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = loginUser.getUsername();
	}
}
