package com.eighteenthstreet.product_service.config;

import static org.springframework.data.redis.serializer.RedisSerializationContext.*;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableCaching
@EnableScheduling
public class ProductCacheConfig {

	@Bean
	public RedisCacheManager cacheManager(
		RedisConnectionFactory connectionFactory,
		RedisSerializer<Object> redisSerializer
	) {
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(120))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeValuesWith(
				SerializationPair.fromSerializer(redisSerializer)
			);

		RedisCacheConfiguration productAllCacheConfig = defaultCacheConfig.entryTtl(Duration.ofMinutes(5));

		RedisCacheConfiguration productCacheConfig = defaultCacheConfig.entryTtl(Duration.ofMinutes(10));

		return RedisCacheManager
			.builder(connectionFactory)
			.cacheDefaults(defaultCacheConfig)
			.withCacheConfiguration("productAllCache", productAllCacheConfig)
			.withCacheConfiguration("productCache", productCacheConfig)
			.build();
	}
}
