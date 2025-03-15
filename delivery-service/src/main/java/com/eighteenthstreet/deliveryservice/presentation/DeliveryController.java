package com.eighteenthstreet.deliveryservice.presentation;

import com.eighteenthstreet.deliveryservice.application.DeliveryService;
import com.eighteenthstreet.deliveryservice.application.dto.CreateDeliveryResponse;
import com.eighteenthstreet.deliveryservice.application.dto.GetDeliveryResponse;
import com.eighteenthstreet.deliveryservice.presentation.request.CreateDeliveryRequest;
import com.eighteenthstreet.deliveryservice.presentation.request.UpdateStatusDeliveryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vi/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    //TODO: 전체적으로 gateWay 가 생기면 jwt 를 받는 헤더를 추가 할 예정, 예외처리도 할 예정

    @PostMapping()
    public ResponseEntity<CreateDeliveryResponse> createDelivery(
            @RequestBody CreateDeliveryRequest request) {
        CreateDeliveryResponse response = deliveryService.createDelivery(request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping()
    public ResponseEntity<Map<String, String>> updateDeliveryStatus(@RequestBody UpdateStatusDeliveryRequest request) {
        deliveryService.updateDeliveryStatus(request);

        return ResponseEntity.ok(Collections.singletonMap("message", "배달 상태가 변경 됐습니다."));
    }

    // TODO: 나중에 Response 를 수정, List<routs> 값하고 배달 담당자도 추가해줘야함
    @GetMapping("/{id}")
    public ResponseEntity<GetDeliveryResponse> getDelivery(@PathVariable("id") UUID uuid) {
        GetDeliveryResponse response = deliveryService.getDelivery(uuid);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDelivery(@PathVariable("id") UUID uuid) {
        deliveryService.deleteDelivery(uuid);

        return ResponseEntity.ok(Collections.singletonMap("message", "배달이 취소되었습니다."));
    }

}
