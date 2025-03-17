package com.eighteenthstreet.hub_service.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.hub_service.domain.HubRepository;
import com.eighteenthstreet.hub_service.domain.model.Hub;

public interface JpaHubRepository extends JpaRepository<Hub, UUID>, HubRepository {
}
