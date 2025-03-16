package com.eighteenthstreet.deliveryservice.application;

import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.GetDeliveryResponse;
import com.eighteenthstreet.deliveryservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryservice.domain.exception.DeliveryNotFoundException;
import com.eighteenthstreet.deliveryservice.domain.model.Delivery;
import com.eighteenthstreet.deliveryservice.domain.repository.DeliveryRepository;
import com.eighteenthstreet.deliveryservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.delivery}")
    private String queueDelivery;

    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest createDeliveryRequest) {
        Delivery delivery = Delivery.createDelivery(createDeliveryRequest);

        DeliveryCreatedEvent event = new DeliveryCreatedEvent(createDeliveryRequest.getStartHubId(), createDeliveryRequest.getEndHubId());
        log.info("######### Send Message[Delivery] : {}", event);
        rabbitTemplate.convertAndSend(queueDelivery, event);

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
