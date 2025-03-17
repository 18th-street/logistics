package com.eighteenthstreet.hub_service.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface JpaHubRepository extends JpaRepository<Hub, UUID>, HubRepository {

	@Override
	boolean existsByName(String name);

	@Override
	Optional<Hub> findById(UUID hubId);
}
