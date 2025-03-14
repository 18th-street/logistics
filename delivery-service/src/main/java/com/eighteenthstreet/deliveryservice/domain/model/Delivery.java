package com.eighteenthstreet.deliveryservice.domain.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

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
@Table(name = "p_delivery")
public class Delivery {

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
	
}
