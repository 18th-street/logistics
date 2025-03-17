package com.eighteenthstreet.hub_service.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.hub_service.application.dto.CreateHubResponse;
import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;
import com.eighteenthstreet.hub_service.exception.CustomHubAlreadyExistException;
import com.eighteenthstreet.hub_service.exception.CustomHubNotFoundException;
import com.eighteenthstreet.hub_service.presentation.request.CreateHubRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "허브 서비스")
@Service
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;

	@Transactional
	public CreateHubResponse createHub(CreateHubRequest request) {

		// todo 허브 중복 검사 후 생성
		if (hubRepository.existsByName(request.getName())) {
			throw new CustomHubAlreadyExistException(ErrorCode.HUB_ALREADY_EXIST);
		}
		// 권한 체크 (Master만 생성 가능)
		Hub hub = Hub.create(request);
		hubRepository.save(hub);

		return CreateHubResponse.from(hub);
	}

	public GetHubResponse getHub(UUID hubId) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		return GetHubResponse.from(hub);
	}


}
