package com.mazemzemi.soldstack.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        String message = "Unauthorized access";
        UnauthorizedException exception = new UnauthorizedException(message);
        assertEquals(message, exception.getMessage());
    }
}
