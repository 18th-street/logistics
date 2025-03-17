package com.eighteenthstreet.slack_service.application.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eighteenthstreet.slack_service.application.dto.SlackMessageResponseDto;
import com.eighteenthstreet.slack_service.domain.model.SlackMessage;

@Mapper(componentModel = "spring")
public interface SlackMessageMapper {
	@Mapping(target = "receiverId", source = "receiverId")
	@Mapping(target = "message", source = "message")
	SlackMessageResponseDto toDto(SlackMessage slackMessage);
}
