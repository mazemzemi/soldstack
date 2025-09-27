package com.mazemzemi.soldstack.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        String message = "Conflict occurred";
        ConflictException exception = new ConflictException(message);
        assertEquals(message, exception.getMessage());
    }
}
