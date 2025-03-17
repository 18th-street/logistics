package com.eighteenthstreet.hub_service.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface HubRepository {

	Optional<Hub> findById(UUID hubId);
}
