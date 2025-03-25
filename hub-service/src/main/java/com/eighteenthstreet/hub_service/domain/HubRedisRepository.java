package com.eighteenthstreet.hub_service.domain;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.eighteenthstreet.hub_service.domain.model.HubCache;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRedisRepository {

	private final RedisTemplate<String, HubCache> redisTemplate;
	private static final String KEY_PREFIX = "hub::";

	public void save(HubCache hub) {
		redisTemplate.opsForValue().set(KEY_PREFIX + hub.getHubId(), hub);
	}

	public HubCache findById(UUID hubId) {
		return redisTemplate.opsForValue().get(KEY_PREFIX + hubId);
	}

	public void delete(UUID hubId) {
		redisTemplate.delete(KEY_PREFIX + hubId);
	}

	public boolean exists(UUID hubId) {
		return redisTemplate.hasKey(KEY_PREFIX + hubId);
	}
}
