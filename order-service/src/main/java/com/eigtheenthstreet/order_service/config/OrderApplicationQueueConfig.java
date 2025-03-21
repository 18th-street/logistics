package com.eigtheenthstreet.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationQueueConfig {
	@Value("${message.exchange}")
	private String exchange;
	@Value("${message.queue.delivery}")
	private String queueDelivery;
	@Value("${message.queue.slack}")
	private String queueSlack;
	@Value("${message.err.exchange}")
	private String exchangeErr;
	@Value("${message.err.queue.order}")
	private String queueErrOrder;
	@Value("${message.complete.exchange}")
	private String exchangeComplete;
	@Value("${message.complete.queue.order}")
	private String queueCompleteOrder;

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public Queue queueDelivery() {
		return new Queue(queueDelivery);
	}

	@Bean
	public Binding bindingDelivery() {
		return BindingBuilder.bind(queueDelivery()).to(exchange()).with(queueDelivery);
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
	public TopicExchange exchangeErr() {
		return new TopicExchange(exchangeErr);
	}

	@Bean
	public Queue queueErrOrder() {
		return new Queue(queueErrOrder);
	}

	@Bean
	public TopicExchange exchangeComplete() {
		return new TopicExchange(exchangeComplete);
	}

	@Bean
	public Queue queueCompleteDelivery() {
		return new Queue(queueCompleteOrder);
	}

	@Bean
	public Binding bindingErrOrder() {
		return BindingBuilder.bind(queueErrOrder()).to(exchangeErr()).with(queueErrOrder);
	}

	@Bean
	public Binding bindingCompleteOrder() {
		return BindingBuilder.bind(queueCompleteDelivery()).to(exchangeComplete()).with(queueCompleteOrder);
	}
}
