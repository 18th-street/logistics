package com.eighteenthstreet.deliveryservice.infrastructure.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryQueueConfig {

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Value("${message.exchange}")
	private String exchange;

	@Value("${message.queue.delivery}")
	private String queueDelivery;

	@Value("${message.queue.route}")
	private String queueRoute;

	@Value("${message.queue.delivery-assigned}")
	private String queueAssigned;

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public Queue queueDelivery() {
		return new Queue(queueDelivery);
	}

	@Bean
	public Queue queueRoute() {
		return new Queue(queueRoute);
	}

	@Bean
	public Queue queueAssigned() {
		return new Queue(queueAssigned);
	}

	@Bean
	public Binding bindingDelivery() {
		return BindingBuilder.bind(queueDelivery()).to(exchange()).with(queueDelivery);
	}

	@Bean
	public Binding bindingRoute() {
		return BindingBuilder.bind(queueRoute()).to(exchange()).with(queueRoute);
	}

	@Bean
	public Binding bindingAssigned() {
		return BindingBuilder.bind(queueRoute()).to(exchange()).with(queueAssigned);
	}
}
