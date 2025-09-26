package com.mazemzemi.soldstack.auth.infrastructure.persistence.token;

import com.mazemzemi.soldstack.auth.application.port.TokenStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenStore implements TokenStore {

    private final StringRedisTemplate redisTemplate;

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

    private String buildKey(String email) {
        return email;
    }
}
