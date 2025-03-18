package com.eighteenthstreet.deliveryservice.infrastructure.repository;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID>, DeliveryRepository {


}
