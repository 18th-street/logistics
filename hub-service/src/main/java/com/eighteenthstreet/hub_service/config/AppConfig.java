package com.eighteenthstreet.hub_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.eighteenthstreet.hub_service.domain.model.HubCache;

@Configuration("hubAppConfig")
public class AppConfig {

	@Bean(name = "hubRedisTemplate")
	public RedisTemplate<String, HubCache> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, HubCache> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());

		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}
}
