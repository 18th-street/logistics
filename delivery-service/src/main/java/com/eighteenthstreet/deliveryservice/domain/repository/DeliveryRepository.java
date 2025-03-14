package com.eighteenthstreet.deliveryservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;

public interface DeliveryRepository {
	Delivery save(Delivery delivery);

	Optional<Delivery> findById(UUID uuid);

	void softDeleteDelivery(UUID uuid);
}
