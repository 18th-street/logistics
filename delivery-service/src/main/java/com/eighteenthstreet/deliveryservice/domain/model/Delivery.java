package com.eighteenthstreet.deliveryservice.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import com.eighteenthstreet.deliveryservice.domain.exception.InvalidDeliveryException;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;

import base.BaseEntity;
import exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {

	@Id
	@UuidGenerator
	private UUID deliveryId;

	@Column(name = "starthub_id")
	private UUID startHubId;

	@Column(name = "endhub_id")
	private UUID endHubId;

	@Column(name = "order_id")
	private UUID orderId;

	@Column(name = "destination_address")
	private String destinationAddress;

	@Column(name = "recipient")
	private String recipient;

	@Column(name = "slack_id")
	private String slackId;

	@Enumerated(value = EnumType.STRING)
	private DeliveryStatus status;

	public void updateStatus(DeliveryStatus deliveryStatus) {
		this.status = deliveryStatus;
	}

	public void cancel() {
		if (this.status == DeliveryStatus.OUT_FOR_DELIVERY || this.status == DeliveryStatus.IN_TRANSIT_TO_VENDOR) {
			throw new InvalidDeliveryException(ErrorCode.INVALID_DELIVERY);
		}

		this.status = DeliveryStatus.CANCELED;
	}

	public static Delivery createDelivery(CreateDeliveryRequest createDeliveryRequest) {
		return Delivery.builder()
			.startHubId(createDeliveryRequest.getStartHubId())
			.endHubId(createDeliveryRequest.getEndHubId())
			.orderId(createDeliveryRequest.getOrderId())
			.recipient(createDeliveryRequest.getRecipient())
			.destinationAddress(createDeliveryRequest.getDestinationAddress())
			.status(DeliveryStatus.PENDING_AT_HUB)
			.build();
	}
}
