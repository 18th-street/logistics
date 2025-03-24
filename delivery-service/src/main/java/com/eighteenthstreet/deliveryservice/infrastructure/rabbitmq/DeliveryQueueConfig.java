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

	@Value("${message.complete.exchange}")
	private String completeExchange;

	// 주문으로 부터 수신 받는 메세지
	@Value("${message.queue.delivery.created}")
	private String queueOrderDelivery;
	@Value("${message.queue.delivery.cancelled}")
	private String queueOrderCancelledDelivery;

	// 배달 삭제시 성공하면 보내주는 메세지
	@Value("${message.complete.queue.delivery.cancelled}")
	private String queueCompleteCancelledDelivery;

	// 배달 삭제시 실패하면 보내주는 메세지
	@Value("${message.err.queue.delivery.cancelled}")
	private String queueErrCancelledDelivery;

	@Value("${message.queue.delivery-service}")
	private String queueDelivery;

	@Value("${message.queue.delivery-route}")
	private String queueRoute;

	@Value("${message.queue.delivery-assigned}")
	private String queueAssigned;

	@Value("${message.queue.delivery-route-failed}")
	private String queueFailed;

	@Value("${message.err.exchange}")
	private String exchangeErr;

	@Value("${message.err.queue.route}")
	private String queueErrRoute;

	@Value("${message.queue.delivery-agent-failed}")
	private String queueAgentFailed;

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	public Queue queueOrderDelivery() {
		return new Queue(queueOrderDelivery);
	}

	@Bean
	public Queue queueOrderCancelledDelivery() {
		return new Queue(queueOrderCancelledDelivery);
	}

	@Bean
	public Queue queueCompleteCancelledDelivery() {
		return new Queue(queueCompleteCancelledDelivery);
	}

	@Bean
	public Queue queueErrCancelledDelivery() {
		return new Queue(queueErrCancelledDelivery);
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
	public Queue queueAgentFailed() {
		return new Queue(queueAgentFailed);
	}

	@Bean
	public Queue queueFailed() {
		return new Queue(queueFailed);
	}

	@Bean
	public Queue queueAssigned() {
		return new Queue(queueAssigned);
	}

	@Bean
	public Binding bindingOrderDelivery() {
		return BindingBuilder.bind(queueOrderDelivery()).to(exchange()).with(queueOrderDelivery);
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

	@Bean
	public Binding bindingQueueOrderCancelledDelivery() {
		return BindingBuilder.bind(queueOrderCancelledDelivery()).to(exchange()).with(queueOrderCancelledDelivery);
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

	@Bean
	public Binding bindingErrCancelledDelivery() {
		return BindingBuilder.bind(queueErrCancelledDelivery()).to(exchangeErr()).with(queueErrCancelledDelivery);
	}

	@Bean
	public Binding bindingAssignedFailed() {
		return BindingBuilder.bind(queueAgentFailed()).to(exchange()).with(queueAgentFailed);
	}

	// 성공 익스체인지
	@Bean
	public TopicExchange completeExchange() {
		return new TopicExchange(completeExchange);
	}

	@Bean
	public Binding bindingQueueCompleteCancelledDelivery() {
		return BindingBuilder.bind(queueCompleteCancelledDelivery())
			.to(exchange())
			.with(queueCompleteCancelledDelivery);
	}
}
