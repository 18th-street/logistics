package com.eighteenthstreet.slack_service.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.eighteenthstreet.slack_service.presentation.dto.UpdateSlackMessageRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_slack_massage")
@SQLRestriction("is_deleted = false")
public class SlackMessage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 30)
	private String receiverId;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID();
		}
	}

	public void update(UpdateSlackMessageRequestDto request) {
		this.message = request.message();
	}

	public void performSoftDelete() {
		this.softDelete();
	}
}
