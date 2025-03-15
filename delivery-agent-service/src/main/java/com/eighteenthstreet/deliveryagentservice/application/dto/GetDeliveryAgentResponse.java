package com.eighteenthstreet.deliveryagentservice.application.dto;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDeliveryAgentResponse {
    private UUID deliveryAgentId;
    private UUID hubId;
    private UUID userId;
    private String slackId;
    private DeliveryAgentType agentType;
    private DeliveryAgentStatus agentStatus;


    public static GetDeliveryAgentResponse fromEntity(DeliveryAgent deliveryAgent) {
        return GetDeliveryAgentResponse.builder()
                .deliveryAgentId(deliveryAgent.getDeliveryAgentId())
                .hubId(deliveryAgent.getHubId())
                .userId(deliveryAgent.getUserId())
                .slackId(deliveryAgent.getSlackId())
                .agentType(deliveryAgent.getDeliveryAgentType())
                .agentStatus(deliveryAgent.getDeliveryAgentTypeStatus())
                .build();
    }
}
