package com.mazemzemi.soldstack.user.application.exception;

import lombok.experimental.StandardException;

import java.util.UUID;

@StandardException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }
}
