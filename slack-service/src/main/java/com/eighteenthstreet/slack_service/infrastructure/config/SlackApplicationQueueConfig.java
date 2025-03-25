package com.eighteenthstreet.slack_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApplicationQueueConfig {
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Value("${message.exchange}")
	private String exchange;

	@Value("${message.queue.slack}")
	private String queueSlack; // 배송 생성 -> Slack

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public Queue queueSlack() {
		return new Queue(queueSlack);
	}

	@Bean
	public Binding bindingSlack() {
		return BindingBuilder.bind(queueSlack()).to(exchange()).with(queueSlack);
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
		ConnectionFactory connectionFactory,
		MessageConverter messageConverter) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setDefaultRequeueRejected(false); //  예외 발생 시 메시지를 다시 큐에 넣지 않음
		return factory;
	}

	// @Bean
	// public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
	// 	DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
	// 	Map<String, Class<?>> idClassMapping = new HashMap<>();
	// 	idClassMapping.put("com.eigtheenthstreet.order_service.infrastructure.messaging.message.NotificationEvent",
	// 		com.eighteenthstreet.slack_service.infrastructure.messaging.message.NotificationEvent.class);
	//
	// 	typeMapper.setIdClassMapping(idClassMapping);
	// 	typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
	//
	// 	Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
	// 	converter.setJavaTypeMapper(typeMapper);
	// 	return converter;
	// }
	//
	// @Bean
	// public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
	// 	Jackson2JsonMessageConverter messageConverter) {
	// 	SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	// 	factory.setConnectionFactory(connectionFactory);
	// 	factory.setMessageConverter(messageConverter);
	// 	return factory;
	// }
}
