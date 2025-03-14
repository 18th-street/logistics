package com.eighteenthstreet.deliveryservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;

public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID>, DeliveryRepository {

	@Modifying
	@Query(nativeQuery = true, value = "UPDATE p_delivery SET deleted_at = current_timestamp "
		+ "WHERE delivery_id = :deliveryId")
	void softDeleteDelivery(@Param("deliveryId") UUID deliveryId);
}
