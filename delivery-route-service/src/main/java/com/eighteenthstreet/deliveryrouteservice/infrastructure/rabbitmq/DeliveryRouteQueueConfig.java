package com.eighteenthstreet.deliveryrouteservice.infrastructure.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryRouteQueueConfig {

	@Value("${message.exchange}")
	private String exchange;

	@Value("${message.complete.exchange}")
	private String completeExchange;

	@Value("${message.queue.delivery-service}")
	private String queueDelivery;

	@Value("${message.queue.delivery-route}")
	private String queueRoute;

	@Value("${message.queue.delivery-route-failed}")
	private String queueFailed;

	@Value("${message.err.exchange}")
	private String exchangeErr;

	@Value("${message.err.queue.route}")
	private String queueErrRoute;

	@Value("${message.complete.queue.order}")
	private String queueCompleteOrder;

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// 기본 익스체인지
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange); // "delivery"
	}

	@Bean
	public TopicExchange completeExchange() {
		return new TopicExchange(completeExchange);
	}

	// 큐 정의
	@Bean
	public Queue queueDelivery() {
		return new Queue(queueDelivery); // "delivery.delivery"
	}

	@Bean
	public Queue queueCompleteOrder() {
		return new Queue(queueCompleteOrder);
	}

	@Bean
	public Queue queueRoute() {
		return new Queue(queueRoute); // "delivery.route"
	}

	@Bean
	public Queue queueFailed() {
		return new Queue(queueFailed); // "delivery.failed"
	}

	// 바인딩
	@Bean
	public Binding bindingDelivery() {
		return BindingBuilder.bind(queueDelivery()).to(exchange()).with(queueDelivery);
	}

	@Bean
	public Binding bindingRoute() {
		return BindingBuilder.bind(queueRoute()).to(exchange()).with(queueRoute);
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

	@Bean
	public Binding bindingCompleteOrder() {
		return BindingBuilder.bind(queueCompleteOrder()).to(completeExchange()).with(queueCompleteOrder);
	}
}