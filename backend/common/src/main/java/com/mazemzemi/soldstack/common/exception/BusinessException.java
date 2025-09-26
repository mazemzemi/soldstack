package com.mazemzemi.soldstack.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

