package com.mazemzemi.soldstack.common.exception;

import com.mazemzemi.soldstack.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundException_shouldReturnCorrectResponse() {
        NotFoundException ex = new NotFoundException("123", "Entity not found");

        ResponseEntity<ApiResponse<?>> response = handler.handleNotFound(ex);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123 with id Entity not found not found.", response.getBody().message());
        assertFalse(response.getBody().success());
    }

    @Test
    void handleBusinessException_shouldReturnCorrectResponse() {
        BusinessException ex = new BusinessException("Business rule failed");

        ResponseEntity<ApiResponse<?>> response = handler.handleBusiness(ex);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business rule failed", response.getBody().message());
        assertFalse(response.getBody().success());
    }
}
