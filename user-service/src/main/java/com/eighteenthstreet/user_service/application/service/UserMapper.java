package com.eighteenthstreet.user_service.application.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eighteenthstreet.user_service.application.dto.UserResponseDto;
import com.eighteenthstreet.user_service.domain.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "userId", source = "userId")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "password", source = "password")
	@Mapping(target = "slackId", source = "slackId")
	@Mapping(target = "name", source = "name")
	@Mapping(target = "phone", source = "phone")
	@Mapping(target = "role", source = "role")
	@Mapping(target = "status", source = "status")
	UserResponseDto toUserResponseDto(User user);
}