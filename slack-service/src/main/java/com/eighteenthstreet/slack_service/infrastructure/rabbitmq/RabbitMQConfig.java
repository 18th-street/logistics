package com.eighteenthstreet.slack_service.infrastructure.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue slackQueue() {
		return new Queue("logistics.slack", true); // durable = true (서버 재시작 시에도 유지)
	}
}