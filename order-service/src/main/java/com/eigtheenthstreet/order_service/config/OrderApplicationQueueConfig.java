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

	@Value("${message.queue.delivery.created}")
	private String queueDeliveryCreated; // 배송 생성 -> Delivery

	@Value("${message.queue.delivery.cancelled}")
	private String queueDeliveryCancelled; // 배송 취소 -> Delivery

	@Value("${message.queue.slack}")
	private String queueSlack; // 배송 생성 -> Slack

	@Value("${message.err.exchange}")
	private String exchangeErr;

	@Value("${message.err.queue.delivery.created}")
	private String queueErrDeliveryCreated; // 배송 생성 실패 Delivery -> Order

	@Value("${message.err.queue.delivery.cancelled}")
	private String queueErrDeliveryCancelled; // 배송 취소 실패 Delivery -> Order

	@Value("${message.complete.exchange}")
	private String exchangeComplete;

	@Value("${message.complete.queue.delivery.created}")
	private String queueCompleteDeliveryCreated; // 배송 생성 성공 Delivery -> Order

	@Value("${message.complete.queue.delivery.cancelled}")
	private String queueCompleteDeliveryCancelled; // 배송 취소 성공 Delivery -> Order

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public Queue queueDeliveryCreated() {
		return new Queue(queueDeliveryCreated);
	}

	@Bean
	public Binding bindingDelivery() {
		return BindingBuilder.bind(queueDeliveryCreated()).to(exchange()).with(queueDeliveryCreated);
	}

	@Bean
	public Queue queueDeliveryCancelled() {
		return new Queue(queueDeliveryCancelled);
	}

	@Bean
	public Binding bindingDeliveryCancelled() {
		return BindingBuilder.bind(queueDeliveryCancelled()).to(exchange()).with(queueDeliveryCancelled);
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
	public Queue queueErrDeliveryCreated() {
		return new Queue(queueErrDeliveryCreated);
	}

	@Bean
	public Binding bindingErrDeliveryCreated() {
		return BindingBuilder.bind(queueErrDeliveryCreated()).to(exchangeErr()).with(queueErrDeliveryCreated);
	}

	@Bean
	public Queue queueErrDeliveryCancelled() {
		return new Queue(queueErrDeliveryCancelled);
	}

	@Bean
	public Binding bindingErrDeliveryCancelled() {
		return BindingBuilder.bind(queueErrDeliveryCancelled()).to(exchangeErr()).with(queueErrDeliveryCancelled);
	}

	@Bean
	public TopicExchange exchangeComplete() {
		return new TopicExchange(exchangeComplete);
	}

	@Bean
	public Queue queueCompleteDeliveryCreated() {
		return new Queue(queueCompleteDeliveryCreated);
	}

	@Bean
	public Binding bindingCompleteDeliveryCreated() {
		return BindingBuilder.bind(queueCompleteDeliveryCreated())
			.to(exchangeComplete())
			.with(queueCompleteDeliveryCreated);
	}

	@Bean
	public Queue queueCompleteDeliveryCancelled() {
		return new Queue(queueCompleteDeliveryCancelled);
	}

	@Bean
	public Binding bindingCompleteDeliveryCancelled() {
		return BindingBuilder.bind(queueCompleteDeliveryCancelled())
			.to(exchangeComplete())
			.with(queueCompleteDeliveryCancelled);
	}
}
