package com.eighteenthstreet.slack_service.infrastructure.slack;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.slack_service.presentation.dto.SendMessageByEmailRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.SendMessageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackClient {

	private static final String SLACK_API_BASE_URL = "https://slack.com/api";
	private static final String CHAT_POST_MESSAGE = SLACK_API_BASE_URL + "/chat.postMessage";
	private static final String LOOKUP_BY_EMAIL = SLACK_API_BASE_URL + "/users.lookupByEmail";

	private final RestTemplate restTemplate;

	@Value("${slack.bot.token}")
	private String slackToken;

	/**
	 * Slack ID를 이용해 Slack 메시지 전송
	 */
	public boolean sendMessage(SendMessageRequestDto request) {
		return sendSlackMessage(request.receiverId(), request.message());
	}

	/**
	 * 이메일을 이용해 Slack 사용자에게 메시지 전송
	 */
	public boolean sendMessageByEmail(SendMessageByEmailRequestDto request) {
		String slackId = getSlackIdByEmail(request.receiverEmail());
		if (slackId == null) {
			log.error("Slack ID 조회 실패: {}", request.receiverEmail());
			return false;
		}
		return sendSlackMessage(slackId, request.message());
	}

	private boolean sendSlackMessage(String receiverId, String message) {
		HttpHeaders headers = createHeaders();
		JSONObject payload = new JSONObject();
		payload.put("channel", receiverId);
		payload.put("text", message);

		HttpEntity<String> entity = new HttpEntity<>(payload.toJSONString(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(CHAT_POST_MESSAGE, HttpMethod.POST, entity,
			String.class);
		return handleSlackResponse(responseEntity);
	}

	private String getSlackIdByEmail(String email) {
		HttpHeaders headers = createHeaders();
		String url = LOOKUP_BY_EMAIL + "?email=" + email;

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

		return parseSlackId(responseEntity);
	}

	private boolean handleSlackResponse(ResponseEntity<String> responseEntity) {
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			log.error("Slack 메시지 전송 실패: HTTP 상태 코드 {}", responseEntity.getStatusCode());
			return false;
		}

		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonResponse = (JSONObject)parser.parse(responseEntity.getBody());
			boolean success = (boolean)jsonResponse.get("ok");

			if (!success) {
				log.error("Slack API 응답 실패: {}", jsonResponse);
				return false;
			}
			return true;

		} catch (ParseException e) {
			log.error("Slack 응답 JSON 파싱 실패", e);
			return false;
		}
	}

	private String parseSlackId(ResponseEntity<String> responseEntity) {
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			log.error("Slack 사용자 조회 실패: HTTP 상태 코드 {}", responseEntity.getStatusCode());
			return null;
		}

		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse(responseEntity.getBody());
			JSONObject user = (JSONObject)jsonObject.get("user");
			return user != null ? (String)user.get("id") : null;

		} catch (ParseException e) {
			log.error("Slack 사용자 ID 조회 JSON 파싱 실패", e);
			return null;
		}
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(slackToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
