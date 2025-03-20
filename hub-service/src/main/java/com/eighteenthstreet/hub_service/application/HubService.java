package com.eighteenthstreet.hub_service.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.hub_service.application.dto.CreateHubResponse;
import com.eighteenthstreet.hub_service.application.dto.GetHubResponse;
import com.eighteenthstreet.hub_service.application.dto.UpdateHubResponse;
import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;
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

	@Transactional
	public CreateHubResponse createHub(CreateHubRequest request) {
		if (hubRepository.existsByName(request.getName())) {
			throw new CustomHubAlreadyExistException(ErrorCode.HUB_ALREADY_EXIST);
		}

		// 권한 체크 (Master만 생성 가능)
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
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		if (Boolean.TRUE.equals(hub.getIsDeleted())) {
			throw new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND);
		}

		return GetHubResponse.from(hub);
	}

	@Transactional
	public UpdateHubResponse updateHub(UUID hubId, UpdateHubRequest request) {
		// 권한 체크 (Master만 수정 가능)
		Hub foundHub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		foundHub.update(request);

		return UpdateHubResponse.from(foundHub);
	}

	@Transactional
	public void deleteHub(UUID hubId) {
		// 권한 체크 (Master만 삭제 가능)
		Hub foundHub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomHubNotFoundException(ErrorCode.HUB_NOT_FOUND));

		foundHub.performSoftDelete();
	}
}
