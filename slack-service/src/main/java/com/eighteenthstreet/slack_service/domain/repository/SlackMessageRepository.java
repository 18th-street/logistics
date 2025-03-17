package com.eighteenthstreet.slack_service.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;

public interface SlackMessageRepository {
	void save(SlackMessage slackMessage);

	List<SlackMessage> findAll();

	Optional<SlackMessage> findById(UUID id);

	Page<SlackMessage> findAllByMessageContains(String word, Pageable customPageable);
}
