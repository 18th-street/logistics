package com.eighteenthstreet.slack_service.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;
import com.eighteenthstreet.slack_service.domain.repository.SlackMessageRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SlackMessageRepositoryImpl implements SlackMessageRepository {
	private final SlackMessageJapRepository slackMessageRepository;

	@Override
	public void save(SlackMessage slackMessage) {
		slackMessageRepository.save(slackMessage);
	}
}
