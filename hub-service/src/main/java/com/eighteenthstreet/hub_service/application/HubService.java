package com.eighteenthstreet.hub_service.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;
import com.eighteenthstreet.hub_service.exception.CustomException;
import com.eighteenthstreet.hub_service.exception.CustomHubNotFoundException;
import com.eighteenthstreet.hub_service.infrastructure.JpaHubRepository;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;

	public GetHubResponse getHub(UUID hubId) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		return GetHubResponse.from(hub);
	}
}
