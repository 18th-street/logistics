package com.eighteenthstreet.slack_service.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;

public interface JpaSlackMessageRepository extends JpaRepository<SlackMessage, UUID> {
	Page<SlackMessage> findAllByMessageContains(String word, Pageable customPageable);
}
