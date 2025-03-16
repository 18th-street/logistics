package com.eighteenthstreet.deliveryrouteservice.application;

import com.eighteenthstreet.deliveryrouteservice.application.dto.GetDeliveryRouteResponse;
import com.eighteenthstreet.deliveryrouteservice.domain.event.DeliveryCreatedEvent;
import com.eighteenthstreet.deliveryrouteservice.domain.exception.DeliveryRouteNotFoundException;
import com.eighteenthstreet.deliveryrouteservice.domain.model.DeliveryRoute;
import com.eighteenthstreet.deliveryrouteservice.domain.repository.DeliveryRouteRepository;
import com.eighteenthstreet.deliveryrouteservice.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {

    private final DeliveryRouteRepository deliveryRouteRepository;

    public void createDeliveryRoute(DeliveryCreatedEvent deliveryCreatedEvent) {

        // 1. TODO: 허브와 허브 경로를 구해야한다. 여기서 (예상 소요시간, 실제거리 ,실제소요시간) 필드 받아와야한다.
        // 2. 그걸 DB에 저장을 한 후
        // 3. DeliveryAgent 로 이벤트 보내준다.

        DeliveryRoute deliveryRoute = DeliveryRoute.createDeliveryRoute(deliveryCreatedEvent);

        deliveryRouteRepository.save(deliveryRoute);
    }

    public GetDeliveryRouteResponse getDeliveryRoutes(UUID deliveryAgentId) {
        DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryAgentId).orElseThrow(
                () -> new DeliveryRouteNotFoundException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND)
        );

        return GetDeliveryRouteResponse.fromEntity(deliveryRoute);
    }

    @Transactional
    public void deleteDeliveryRoute(UUID deliveryAgentId) {
        DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryAgentId).orElseThrow(
                () -> new DeliveryRouteNotFoundException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND)
        );

        deliveryRoute.onPreRemove();
        deliveryRouteRepository.save(deliveryRoute);
    }
}
