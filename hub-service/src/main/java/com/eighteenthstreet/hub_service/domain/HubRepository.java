package com.eighteenthstreet.hub_service.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface HubRepository {

	boolean existsByName(String name);

	Hub save(Hub hub);

	Page<Hub> findAll(Pageable pageable);

	Page<Hub> findByNameContaining(String keyword, Pageable pageable);

	Optional<Hub> findById(UUID hubId);
}
