package com.eighteenthstreet.deliveryrouteservice.presentation;


import com.eighteenthstreet.deliveryrouteservice.application.DeliveryRouteService;
import com.eighteenthstreet.deliveryrouteservice.application.dto.GetDeliveryRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

    @GetMapping("/{id}")
    public ResponseEntity<GetDeliveryRouteResponse> getDeliveryRoute(@PathVariable("id") UUID deliveryAgentId) {
        GetDeliveryRouteResponse response = deliveryRouteService.getDeliveryRoutes(deliveryAgentId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDeliveryRoute(@PathVariable("id") UUID deliveryAgentId) {
        deliveryRouteService.deleteDeliveryRoute(deliveryAgentId);

        return ResponseEntity.ok(Collections.singletonMap("message", "배달경로가 삭제되었습니다."));
    }


}
