package com.eighteenthstreet.deliveryrouteservice.application.dto;

import java.util.UUID;

import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetDeliveryRouteResponse {

	private Integer sequence;
	private UUID startHubId;
	private UUID endHubId;
	private Double estimatedDistance;
	private Double estimatedDuration;

	public static GetDeliveryRouteResponse fromEntity(DeliveryRoute deliveryRoute) {
		return GetDeliveryRouteResponse.builder()
			.sequence(deliveryRoute.getSequence())
			.startHubId(deliveryRoute.getStartHubId())
			.endHubId(deliveryRoute.getEndHubId())
			.estimatedDistance(deliveryRoute.getEstimatedDistance())
			.estimatedDuration(deliveryRoute.getEstimatedDuration())
			.build();
	}

}
