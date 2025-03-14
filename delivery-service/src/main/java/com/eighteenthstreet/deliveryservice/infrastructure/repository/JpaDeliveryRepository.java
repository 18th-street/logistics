package com.eighteenthstreet.deliveryservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;

public interface JpaDeliveryRepository extends DeliveryRepository, JpaRepository<Delivery, UUID> {

}
