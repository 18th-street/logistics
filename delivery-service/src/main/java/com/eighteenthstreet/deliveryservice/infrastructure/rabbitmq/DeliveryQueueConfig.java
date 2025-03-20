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

	@Value("${message.queue.failed}")
	private String queueFailed;

	@Value("${message.err.exchange}")
	private String exchangeErr;

	@Value("${message.err.queue.route}")
	private String queueErrRoute;

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
	public Queue queueFailed() {
		return new Queue(queueFailed); // "delivery.failed"
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

	@Bean
	public Binding bindingFailed() {
		return BindingBuilder.bind(queueFailed()).to(exchange()).with(queueFailed);
	}

	// 에러 익스체인지
	@Bean
	public TopicExchange exchangeErr() {
		return new TopicExchange(exchangeErr); // "delivery.err"
	}

	// 에러 큐
	@Bean
	public Queue queueErrRoute() {
		return new Queue(queueErrRoute); // "delivery.err.route"
	}

	// 에러 바인딩
	@Bean
	public Binding bindingErrRoute() {
		return BindingBuilder.bind(queueErrRoute()).to(exchangeErr()).with(queueErrRoute);
	}
}
