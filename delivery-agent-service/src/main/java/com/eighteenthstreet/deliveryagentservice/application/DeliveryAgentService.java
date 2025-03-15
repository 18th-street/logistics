package com.eighteenthstreet.deliveryagentservice.application;

import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryAgentService {

    private final DeliveryAgentRepository deliveryAgentRepository;

    //TODO: 추후 동기방식으로 boolean 값으로 검증 하고 저장함
    // 유저에서 DeliveryAgent 을 생성
    public CreateDeliveryAgentResponse createDeliveryAgent(CreateDeliveryAgentRequest request) {

        // 1. 추후 hubService 에서 ID 검증을 해봐야함
        // 2. useService 에서도 ID 검증을 해봐야함
        DeliveryAgent deliveryAgent = DeliveryAgent.builder()
                .hubId(request.getHubId())
                .userId(request.getUserId())
                .deliveryAgentType(request.getDeliveryAgentType())
                .deliveryAgentTypeStatus(DeliveryAgentStatus.AVAILABLE)
                .slackId(request.getSlackId())
                .build();

        return CreateDeliveryAgentResponse.fromEntity(deliveryAgentRepository.save(deliveryAgent));
    }
}
