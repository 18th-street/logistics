package com.eighteenthstreet.hub_service.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.hub_service.application.dto.CreateHubResponse;
import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.application.dto.UpdateHubResponse;
import com.eighteenthstreet.hub_service.domain.HubRedisRepository;
import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;
import com.eighteenthstreet.hub_service.domain.model.HubCache;
import com.eighteenthstreet.hub_service.exception.CustomHubAlreadyExistException;
import com.eighteenthstreet.hub_service.exception.CustomHubNotFoundException;
import com.eighteenthstreet.hub_service.presentation.request.CreateHubRequest;
import com.eighteenthstreet.hub_service.presentation.request.UpdateHubRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "허브 서비스")
@Service
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final HubRedisRepository hubRedisRepository;

	@Transactional
	public CreateHubResponse createHub(CreateHubRequest request) {
		if (hubRepository.existsByName(request.getName())) {
			throw new CustomHubAlreadyExistException(ErrorCode.HUB_ALREADY_EXIST);
		}

		Hub hub = Hub.create(request);
		hubRepository.save(hub);

		return CreateHubResponse.from(hub);
	}

	@Transactional(readOnly = true)
	public PagedModel<GetHubResponse> searchHubs(PageRequest pageable, String keyword) {
		Page<Hub> hubPage;

		if (keyword == null || keyword.trim().isEmpty()) {
			hubPage = hubRepository.findByIsDeletedNull(pageable);
		} else {
			hubPage = hubRepository.findByIsDeletedNullAndNameContaining(keyword, pageable);
		}

		Page<GetHubResponse> responses = hubPage.map(GetHubResponse::from);

		return new PagedModel<>(responses);
	}

	@Transactional(readOnly = true)
	public GetHubResponse getHub(UUID hubId) {
		HubCache cached = hubRedisRepository.findById(hubId);

		if (cached != null) {
			return GetHubResponse.fromCahce(cached);
		}

		Hub hub = hubRepository.findByHubIdAndIsDeletedNull(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		return GetHubResponse.from(hub);
	}

	@Transactional
	public UpdateHubResponse updateHub(UUID hubId, UpdateHubRequest request) {
		Hub foundHub = hubRepository.findByHubIdAndIsDeletedNull(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		foundHub.update(request);

		return UpdateHubResponse.from(foundHub);
	}

	@Transactional
	public void deleteHub(UUID hubId) {
		Hub foundHub = hubRepository.findByHubIdAndIsDeletedNull(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		foundHub.performSoftDelete();
	}

	@Transactional(readOnly = true)
	public List<GetHubResponse> getHubsById(List<UUID> hubIds) {
		List<Hub> hubs = hubRepository.findByHubIdInAndIsDeletedNull(hubIds);

		return hubs.stream().map(GetHubResponse::from).toList();
	}

	public boolean existsById(UUID hubId) {
		return hubRepository.existsById(hubId);
	}
}
