package com.eighteenthstreet.hub_service.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface HubRepository {

	boolean existsByName(String name);

	Hub save(Hub hub);

	Page<Hub> findByIsDeletedNull(PageRequest pageable);

	Page<Hub> findByIsDeletedNullAndNameContaining(String keyword, PageRequest pageable);

	Optional<Hub> findById(UUID hubId);
}
