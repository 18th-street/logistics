package com.eighteenthstreet.slack_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.eighteenthstreet.slack_service.application.service.SlackService;
import com.eighteenthstreet.slack_service.infrastructure.messaging.message.NotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageListener {
	private final SlackService slackService;

	@RabbitListener(queues = "${message.queue.slack}")
	public void onNotificationEvent(NotificationEvent event) {
		log.info("[SlackMessageListener] queue.slack 큐에 orderId={}를 수신 받았습니다.", event.orderId());
		slackService.handleNotificationEvent(event);
	}

}
