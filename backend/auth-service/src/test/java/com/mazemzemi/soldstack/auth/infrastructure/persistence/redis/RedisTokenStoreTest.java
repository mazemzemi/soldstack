package com.mazemzemi.soldstack.auth.infrastructure.persistence.redis;

import com.mazemzemi.soldstack.auth.infrastructure.persistence.token.RedisTokenStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisTokenStoreTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisTokenStore redisTokenStore;

    @Test
    @DisplayName("storeToken should call redisTemplate with correct arguments")
    void storeToken_shouldCallRedisTemplate() {
        // Arrange
        String email = "test@example.com";
        String token = "jwt-token";
        long ttl = 3600;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        redisTokenStore.storeToken(email, token, ttl);

        // Assert
        // Verify that the set method was called on the ValueOperations mock with the correct parameters.
        verify(valueOperations, times(1)).set(email, token, ttl, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("getToken should retrieve token from redisTemplate")
    void getToken_shouldRetrieveToken() {
        // Arrange
        String email = "test@example.com";
        String expectedToken = "jwt-token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(email)).thenReturn(expectedToken);

        // Act
        String actualToken = redisTokenStore.getToken(email);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(valueOperations, times(1)).get(email);
    }

    @Test
    @DisplayName("removeToken should call delete on redisTemplate")
    void removeToken_shouldCallDelete() {
        // Arrange
        String email = "test@example.com";

        // Act
        redisTokenStore.removeToken(email);

        // Assert
        // Verify that the delete method was called directly on the redisTemplate mock.
        verify(redisTemplate, times(1)).delete(email);
    }
}