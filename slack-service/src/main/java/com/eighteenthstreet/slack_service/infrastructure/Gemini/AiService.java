package com.eighteenthstreet.slack_service.infrastructure.Gemini;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.slack_service.presentation.dto.DeliveryMessageRequestDto;
import com.eighteenthstreet.slack_service.presentation.dto.OrderMessageRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AiService {
	private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

	@Value("${GEMINI_API_KEY}")
	private String apiKey;

	public String getFinalShippingDeadline(OrderMessageRequestDto order, DeliveryMessageRequestDto delivery) {
		RestTemplate restTemplate = new RestTemplate();

		String text = String.format(
			"상품 : %s, 수량: %d, 요청 사항: %s, 출발 허브: %s, 도착 허브: %s, 도착지 주소: %s, 배송 담당자 근무시간: 9:00-18:00. "
				+ "주어진 정보와 중간에 허브를 경유할 수 있다는 점을 고려해서 최종 발송 시한을 구체적으로 계산해주세요. "
				+ "발송허브 담당자에게 알려줘야 합니다. 답변은 딱 한 줄로 '전달된 정보를 기반으로 도출된 최종 발송 시한은 00월 00일 오전 00시 입니다.'이런 식으로 답해주세요.",
			order.productName(), order.quantity(), order.requestDetails(), delivery.startHub(), delivery.endHub(),
			delivery.destinationAddress()
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("contents", List.of(
			Map.of("parts", List.of(Map.of("text", text)))
		));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// API 요청 생성
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		// API 요청 실행
		String response = restTemplate.postForObject(GEMINI_API_URL + "?key=" + apiKey, requestEntity, String.class);

		log.info("Gemini Response: {}", response);

		return response;
	}

	public String extractFinalDeadlineMessage(String geminiResponse) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(geminiResponse);

			// "candidates[0].content.parts[0].text" 값 추출
			return rootNode.path("candidates").get(0)
				.path("content").path("parts").get(0)
				.path("text").asText();
		} catch (Exception e) {
			return "최종 발송 시한 정보를 가져올 수 없습니다.";
		}
	}
}
