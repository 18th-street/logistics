package com.eighteenthstreet.deliveryagentservice.infrastructure;

import com.eighteenthstreet.deliveryagentservice.application.DeliveryAgentService;
import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/delivery-agents")
@RequiredArgsConstructor
public class DeliveryAgentController {
    private final DeliveryAgentService deliveryAgentService;

    //TODO: 추후 JWT 가져와서 권한체크 필요

    @PostMapping
    public ResponseEntity<CreateDeliveryAgentResponse> createDeliveryAgent(@RequestBody CreateDeliveryAgentRequest request) {
        CreateDeliveryAgentResponse response = deliveryAgentService.createDeliveryAgent(request);

        return ResponseEntity.ok(response);
    }


}
