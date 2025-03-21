package com.eighteenthstreet.slack_service.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.slack_service.application.dto.SlackMessageResponseDto;
import com.eighteenthstreet.slack_service.application.service.SlackService;
import com.eighteenthstreet.slack_service.infrastructure.rabbitmq.SlackEndpoint;
import com.eighteenthstreet.slack_service.presentation.dto.DeliveryMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.OrderMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.UpdateSlackMessageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slacks")
public class SlackController {
	private final SlackService slackService;
	private final SlackEndpoint slackEndpoint;

	@Description(
		"슬랙 멤버 ID로 슬랙 메시지 전송"
	)
	@PostMapping
	public ResponseEntity<Void> sendMessageToId(@RequestBody SendMessageRequestDto request) {
		slackService.sendSlackMessage(request);
		return ResponseEntity.ok().build();
	}

	@Description(
		"email로 슬랙 메시지 전송"
	)
	@PostMapping("/email")
	public ResponseEntity<Void> sendMessageByEmail(@RequestBody SendMessageByEmailRequestDto request) {
		slackService.sendSlackMessageByEmail(request);
		return ResponseEntity.ok().build();
	}

	@Description(
		"슬랙 메시지 전체 조회"
	)
	@GetMapping
	public ResponseEntity<List<SlackMessageResponseDto>> getAllSlackMessages() {
		return ResponseEntity.ok(slackService.getAllSlackMessages());
	}

	@Description(
		"슬랙 메시지 상세 조회"
	)
	@GetMapping("/{id}")
	public ResponseEntity<SlackMessageResponseDto> getSlackMessages(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(slackService.getSlackMessages(id));
	}

	@Description(
		"슬랙 메시지 검색"
	)
	@GetMapping("/search")
	public ResponseEntity<Page<SlackMessageResponseDto>> searchSlackMessages(
		@RequestParam(name = "word", defaultValue = "") String word,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "createdAt") String sortField,
		@RequestParam(name = "direction", defaultValue = "DESC") String direction,
		Pageable pageable
	) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}
		Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable customPageable = PageRequest.of(
			pageable.getPageNumber(),
			size,
			Sort.by(sortDirection, sortField)
		);

		return ResponseEntity.ok(slackService.searchSlackMessages(word, customPageable));
	}

	@Description(
		"슬랙 메시지 수정"
	)
	@PatchMapping("/{id}")
	public ResponseEntity<SlackMessageResponseDto> updateSlackMessage(
		@PathVariable("id") UUID id,
		@RequestBody UpdateSlackMessageRequestDto request
	) {
		return ResponseEntity.ok(slackService.updateSlackMessage(id, request));
	}

	@Description(
		"슬랙 메시지 소프트 삭제"
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSlackMessage(@PathVariable("id") UUID id) {
		slackService.deleteSlackMessage(id);
		return ResponseEntity.ok().build();
	}

	@Description("For Test")
	@PostMapping("/both")
	public ResponseEntity<Void> sendMessageToHubManger(
		@RequestParam("slackId") String slackId,
		@RequestBody OrderAndDeliveryRequestDto requestDto
	) {
		slackService.sendSlackMessageToHubManager(slackId, requestDto.order, requestDto.delivery);
		return ResponseEntity.ok().build();
	}

	public record OrderAndDeliveryRequestDto(
		OrderMessageRequestDto order,
		DeliveryMessageRequestDto delivery
	) {
	}

	@PostMapping("/test/send")
	public void test() {
		slackEndpoint.sendSlackToHubManager(UUID.randomUUID());
	}

}
