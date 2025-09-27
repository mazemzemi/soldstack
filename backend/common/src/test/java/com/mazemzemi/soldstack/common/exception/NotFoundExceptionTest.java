package com.mazemzemi.soldstack.common.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

    @Test
    void constructor_shouldSetMessageAndId() {
        String id = "123";
        String message = "Entity not found";

        NotFoundException exception = new NotFoundException(id, message);

        assertEquals("123 with id Entity not found not found.", exception.getMessage());
    }
}

