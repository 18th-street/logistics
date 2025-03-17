package com.eighteenthstreet.deliveryservice.domain.model;


import base.BaseEntity;
import com.eighteenthstreet.deliveryservice.domain.exception.InvalidDeliveryException;
import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

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
                .slackId(createDeliveryRequest.getRecipientSlackId())
                .status(DeliveryStatus.PENDING_AT_HUB)
                .build();
    }
}
