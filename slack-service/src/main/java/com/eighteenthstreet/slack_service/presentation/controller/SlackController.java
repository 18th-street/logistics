package com.eighteenthstreet.slack_service.presentation.controller;

import java.util.List;
import java.util.UUID;

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
import com.eighteenthstreet.slack_service.infrastructure.messaging.SlackMessageListener;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.UpdateSlackMessageRequestDto;

import auth.CheckRole;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slacks")
@Tag(name = "Slack-Controller", description = "Slack CRUD API 엔트포인트")
public class SlackController {
	private final SlackService slackService;
	private final SlackMessageListener slackMessageListener;

	@Operation(summary = "슬랙 메시지 전송 (By slackID)", description = "Slack ID로 슬랙 메시지 전송")
	@PostMapping
	public ResponseEntity<Void> sendMessageToId(
		@Parameter(description = "슬랙 메시지 전송 요청 데이터") @RequestBody SendMessageRequestDto request) {
		slackService.sendSlackMessage(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "슬랙 메시지 전송 (By email)", description = "Email로 슬랙 메시지 전송")
	@PostMapping("/email")
	public ResponseEntity<Void> sendMessageByEmail(
		@Parameter(description = "슬랙 메시지 전송 요청 데이터") @RequestBody SendMessageByEmailRequestDto request) {
		slackService.sendSlackMessageByEmail(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "슬랙 메시지 전체 조회", description = "삭제된 슬랙 제외한 모든 슬랙 메시지 조회")
	@CheckRole(Role.MASTER)
	@GetMapping
	public ResponseEntity<List<SlackMessageResponseDto>> getAllSlackMessages() {
		return ResponseEntity.ok(slackService.getAllSlackMessages());
	}

	@Operation(summary = "슬랙 상세 조회", description = "전달받은 id에 해당하는 슬랙 메시지 정보 상세 조회")
	@CheckRole(Role.MASTER)
	@GetMapping("/{id}")
	public ResponseEntity<SlackMessageResponseDto> getSlackMessages(
		@Parameter(description = "조회할 슬랙 메시지 ID") @PathVariable("id") UUID id) {
		return ResponseEntity.ok(slackService.getSlackMessages(id));
	}

	@Operation(summary = "슬랙 메시지 검색", description = "메시지 내용에 전달받은 word에 해당하는 슬랙 메시지 정보 조회")
	@CheckRole(Role.MASTER)
	@GetMapping("/search")
	public ResponseEntity<Page<SlackMessageResponseDto>> searchSlackMessages(
		@Parameter(description = "메시지 내용에서 검색할 단어") @RequestParam(name = "word", defaultValue = "") String word,
		@Parameter(description = "한 페이지에서 볼 개수 ") @RequestParam(name = "size", defaultValue = "10") int size,
		@Parameter(description = "정렬 기준 ") @RequestParam(name = "sort", defaultValue = "createdAt") String sortField,
		@Parameter(description = "정렬 방식") @RequestParam(name = "direction", defaultValue = "DESC") String direction,
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

	@Operation(summary = "슬랙 메시지 수정", description = "수정 요청 받은 데이터를 기반으로 슬랙 메시지 수정")
	@CheckRole(Role.MASTER)
	@PatchMapping("/{id}")
	public ResponseEntity<SlackMessageResponseDto> updateSlackMessage(
		@Parameter(description = "수정할 슬랙 메시지 ID") @PathVariable("id") UUID id,
		@Parameter(description = "슬랙 메시지 수정 요청 데이터") @RequestBody UpdateSlackMessageRequestDto request
	) {
		return ResponseEntity.ok(slackService.updateSlackMessage(id, request));
	}

	@Operation(summary = "슬랙 메시지 삭제", description = "요청 받은 id에 해당하는 슬랙 메시지 삭제")
	@CheckRole(Role.MASTER)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSlackMessage(
		@Parameter(description = "삭제할 슬랙 메시지 ID") @PathVariable("id") UUID id) {
		slackService.deleteSlackMessage(id);
		return ResponseEntity.ok().build();
	}

	// @PostMapping("/test/send/{orderId}")
	// public void test(@PathVariable("orderId") UUID orderId) {
	// 	slackMessageListener.sendSlackToHubManager(orderId);
	// }

}
