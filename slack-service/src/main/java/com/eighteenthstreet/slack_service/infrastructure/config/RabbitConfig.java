package com.eighteenthstreet.slack_service.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
		Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("com.eigtheenthstreet.order_service.infrastructure.messaging.message.NotificationEvent",
			com.eighteenthstreet.slack_service.infrastructure.messaging.message.NotificationEvent.class);

		typeMapper.setIdClassMapping(idClassMapping);
		typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		converter.setJavaTypeMapper(typeMapper);
		return converter;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
		Jackson2JsonMessageConverter messageConverter) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		return factory;
	}
}
