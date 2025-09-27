package com.mazemzemi.soldstack.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        String message = "Invalid email or password";
        InvalidCredentialsException exception = new InvalidCredentialsException(message);
        assertEquals(message, exception.getMessage());
    }
}

