package com.mazemzemi.soldstack.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForbiddenExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        String message = "Access denied";
        ForbiddenException exception = new ForbiddenException(message);
        assertEquals(message, exception.getMessage());
    }
}
