package com.eighteenthstreet.deliveryagentservice.presentation;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.deliveryagentservice.application.DeliveryAgentService;
import com.eighteenthstreet.deliveryagentservice.application.dto.CreateDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.application.dto.GetDeliveryAgentResponse;
import com.eighteenthstreet.deliveryagentservice.presentation.request.CreateDeliveryAgentRequest;
import com.eighteenthstreet.deliveryagentservice.presentation.request.UpdateDeliveryTypeRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/delivery-agents")
@RequiredArgsConstructor
public class DeliveryAgentController {
	private final DeliveryAgentService deliveryAgentService;

	//TODO: 추후 JWT 가져와서 권한체크 필요

	@PostMapping
	public ResponseEntity<CreateDeliveryAgentResponse> createDeliveryAgent(
		@RequestBody CreateDeliveryAgentRequest request) {
		CreateDeliveryAgentResponse response = deliveryAgentService.createDeliveryAgent(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("{id}")
	public ResponseEntity<GetDeliveryAgentResponse> getDeliveryAgent(@PathVariable(name = "id") UUID id) {
		GetDeliveryAgentResponse response = deliveryAgentService.getDeliveryAgent(id);

		return ResponseEntity.ok(response);
	}

	@PatchMapping()
	public ResponseEntity<Map<String, String>> updateDeliveryAgentType(@RequestBody UpdateDeliveryTypeRequest request) {
		deliveryAgentService.updateDeliveryAgentType(request);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달담당자 타입이 변경되었습니다."));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Map<String, String>> deleteDeliveryAgent(@PathVariable("id") UUID id) {
		deliveryAgentService.deleteDeliveryAgent(id);

		return ResponseEntity.ok(Collections.singletonMap("message", "배달담당자가 삭제되었습니다."));
	}

}
