package com.eighteenthstreet.slack_service.domain.repository;

import java.util.List;

import com.eighteenthstreet.slack_service.domain.model.SlackMessage;

public interface SlackMessageRepository {
	void save(SlackMessage slackMessage);

	List<SlackMessage> findAll();
}
