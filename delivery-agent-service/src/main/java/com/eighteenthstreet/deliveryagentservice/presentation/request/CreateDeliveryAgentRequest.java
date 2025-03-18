package com.eighteenthstreet.deliveryagentservice.presentation.request;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDeliveryAgentRequest {
    private UUID hubId;
    private UUID userId;
    private DeliveryAgentType deliveryAgentType;
    private String slackId;
}
