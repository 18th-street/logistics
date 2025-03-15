package com.eighteenthstreet.deliveryagentservice.application;

import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.application.dto.GetDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.domain.exception.DeliveryAgentNotFoundException;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgent;
import com.eighteenthstreet.deliveryagentservice.domain.model.DeliveryAgentStatus;
import com.eighteenthstreet.deliveryagentservice.domain.repository.DeliveryAgentRepository;
import com.eighteenthstreet.deliveryagentservice.presentation.exception.ErrorCode;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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


    public GetDeliveryAgentResponse getDeliveryAgent(UUID id) {
        DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(id).orElseThrow(
                () -> new DeliveryAgentNotFoundException(ErrorCode.DELIVERY_AGENT_NOT_FOUND)
        );

        return GetDeliveryAgentResponse.fromEntity(deliveryAgent);
    }


    //TODO: 적당한 담당자 찾는 로직 구현
    public void handleRouteCreated() {
        // 1. 적당한 담당자 찾기
        // 2. 배차정보 set
        // 3. DB저장
        // 4. Delivery로 이벤트 발행 --> 그러면 Delivery 상태 변경
    }
}
