package com.mazemzemi.soldstack.auth.infrastructure.persistence.token;

import com.mazemzemi.soldstack.auth.application.port.TokenStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenStore implements TokenStore {

    private final StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_PREFIX = "BLACKLIST_";

    public RedisTokenStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void storeToken(String email, String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(buildKey(email), token, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getToken(String email) {
        return redisTemplate.opsForValue().get(buildKey(email));
    }

    @Override
    public void removeToken(String email) {
        redisTemplate.delete(buildKey(email));
    }

    public void blacklistToken(String email, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + getToken(email),
                "true",
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
    private String buildKey(String email) {
        return email;
    }
}
