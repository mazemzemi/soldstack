package com.mazemzemi.soldstack.common.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String resource, Object id) {
        super(resource + " with id " + id + " not found.");
    }
}
