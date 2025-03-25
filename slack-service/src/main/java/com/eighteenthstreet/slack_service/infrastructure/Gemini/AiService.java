package com.eighteenthstreet.slack_service.infrastructure.Gemini;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighteenthstreet.slack_service.application.dto.OrderDeliveryInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AiService {
	private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

	@Value("${GEMINI_API_KEY}")
	private String apiKey;

	public String getFinalShippingDeadline(OrderDeliveryInfo request) {
		RestTemplate restTemplate = new RestTemplate();

		// 상품 정보 포맷 (여러 개 가능, 쉼표로 구분)
		String productDetails = IntStream.range(0, request.productName().size())
			.mapToObj(i -> String.format("%s (수량: %d)", request.productName().get(i), request.productQuantity().get(i)))
			.collect(Collectors.joining(", "));

		// 경유지 포맷 (1개 이상 가능)
		String stoppingText = request.stopping().isEmpty() ? "경유지 없음" : String.join(" -> ", request.stopping());

		// AI에 전달할 프롬프트 생성
		String text = String.format(
			"상품: %s, 고객은 %s까지 받고 싶어 하고, 출발 허브: %s , 경유 허브: %s , 도착 허브: %s , 도착지 주소: %s, 배송 담당자 근무시간: 9:00-18:00."
				+ "주어진 정보와 고객의 요청사항, 경유할 수 있다는 점을 최선으로 고려해서 가장 빠른 최종 발송 시한을 구체적으로 계산해주세요.(요청 사항이 없는 경우 현재 시간을 기준으로 가장 최적 발송 시한 계산)"
				+ "발송허브 담당자에게 알려줘야 합니다. 답변은 딱 한 줄로 '전달된 정보를 기반으로 도출된 최종 발송 시한은 00월 00일 오전 00시 입니다.' 이런 식으로 답해주세요.",
			request.orderId(),
			productDetails,
			request.requests() != null ? request.requests() : "없음",
			request.start(),
			stoppingText,
			request.destination(),
			request.destination()
		);

		// API 요청 바디 생성
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
