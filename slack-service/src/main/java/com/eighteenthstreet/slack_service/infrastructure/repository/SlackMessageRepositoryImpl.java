package com.eighteenthstreet.slack_service.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;
import com.eighteenthstreet.slack_service.domain.repository.SlackMessageRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SlackMessageRepositoryImpl implements SlackMessageRepository {
	private final JpaSlackMessageRepository jpaSlackMessageRepository;

	@Override
	public void save(SlackMessage slackMessage) {
		jpaSlackMessageRepository.save(slackMessage);
	}

	@Override
	public List<SlackMessage> findAll() {
		return jpaSlackMessageRepository.findAll();
	}

	@Override
	public Optional<SlackMessage> findById(UUID id) {
		return jpaSlackMessageRepository.findById(id);
	}

	@Override
	public Page<SlackMessage> findAllByMessageContains(String word, Pageable customPageable) {
		return jpaSlackMessageRepository.findAllByMessageContains(word, customPageable);
	}
}
