package com.eighteenthstreet.deliveryservice.domain.repository;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID uuid);

}
