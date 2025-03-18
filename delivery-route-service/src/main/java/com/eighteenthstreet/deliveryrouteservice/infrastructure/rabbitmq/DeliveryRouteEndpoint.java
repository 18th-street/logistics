package com.eighteenthstreet.deliveryrouteservice.infrastructure.rabbitmq;

import com.eighteenthstreet.deliveryrouteservice.application.DeliveryRouteService;
import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryRouteEndpoint {

    private final DeliveryRouteService deliveryRouteService;

    @RabbitListener(queues = "${message.queue.delivery}")
    public void receiveMessage(DeliveryCreatedEvent deliveryCreatedEvent) {
        log.info("####### Receive Message[Delivery-Route]: {}", deliveryCreatedEvent);

        deliveryRouteService.createDeliveryRoute(deliveryCreatedEvent);
    }
}
