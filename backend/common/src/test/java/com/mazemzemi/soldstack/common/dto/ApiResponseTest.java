package com.mazemzemi.soldstack.common.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void success_shouldReturnApiResponseWithDataAndMessage() {
        String data = "Hello";
        String message = "Success";

        ApiResponse<String> response = ApiResponse.success(data, message);

        assertTrue(response.success());
        assertEquals(data, response.data());
        assertEquals(message, response.message());
    }

    @Test
    void failure_shouldReturnApiResponseWithMessageOnly() {
        String message = "Error occurred";

        ApiResponse<String> response = ApiResponse.error(message);

        assertFalse(response.success());
        assertNull(response.data());
        assertEquals(message, response.message());
    }
}

