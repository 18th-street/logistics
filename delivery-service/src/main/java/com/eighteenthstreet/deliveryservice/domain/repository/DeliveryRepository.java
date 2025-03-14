package com.eighteenthstreet.deliveryservice.domain.repository;

import com.eighteenthstreet.deliveryservice.domain.model.Delivery;

public interface DeliveryRepository {
	Delivery save(Delivery delivery);
}
