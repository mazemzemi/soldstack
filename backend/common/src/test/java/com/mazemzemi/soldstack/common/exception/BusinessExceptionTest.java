package com.mazemzemi.soldstack.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusinessExceptionTest {

    @Test
    void constructor_shouldSetMessageAndId() {
        String id = "123";
        String message = "Business rule violated";

        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
    }
}
