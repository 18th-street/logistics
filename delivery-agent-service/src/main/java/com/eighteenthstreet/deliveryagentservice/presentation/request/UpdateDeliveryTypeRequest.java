package com.eighteenthstreet.deliveryagentservice.presentation.request;

import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDeliveryTypeRequest {
    private UUID deliveryAgentId;
    private DeliveryAgentType deliveryAgentType;

}
