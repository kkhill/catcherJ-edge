package com.kkhill.core.exception;

/**
 * not found thing, service, property
 */
public class NotFoundException extends Exception {
    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
}
