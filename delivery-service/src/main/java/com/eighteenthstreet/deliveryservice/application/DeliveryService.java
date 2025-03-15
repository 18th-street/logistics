package com.eighteenthstreet.deliveryservice.application;

import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.GetDeliveryResponse;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.model.DeliveryStatus;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest createDeliveryRequest) {
        Delivery delivery = Delivery.builder()
                .startHubId(createDeliveryRequest.getStartHubId())
                .endHubId(createDeliveryRequest.getEndHubId())
                .orderId(createDeliveryRequest.getOrderId())
                .recipient(createDeliveryRequest.getRecipient())
                .destinationAddress(createDeliveryRequest.getDestinationAddress())
                .slackId(createDeliveryRequest.getRecipientSlackId())
                .status(DeliveryStatus.PENDING_AT_HUB)
                .build();

        return CreateDeliveryResponse.fromEntity(deliveryRepository.save(delivery));
    }

    @Transactional
    public void updateDeliveryStatus(UpdateStatusDeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

        delivery.updateStatus(request.getDeliveryStatus());
        deliveryRepository.save(delivery);
    }

    //TODO: hubClient 에서 허브경로 받아와서 저장하고, 배달담당자도 받아와야함
    public GetDeliveryResponse getDelivery(UUID uuid) {
        Delivery delivery = deliveryRepository.findById(uuid)
                .orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

        return GetDeliveryResponse.fromEntity(delivery);
    }

    @Transactional
    public void deleteDelivery(UUID uuid) {
        Delivery delivery = deliveryRepository.findById(uuid)
                .orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

        delivery.cancel();

        deliveryRepository.softDeleteDelivery(uuid);
    }
}
