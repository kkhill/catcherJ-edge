package com.kkhill.core.exception;

public class ServiceNotFoundException extends Exception {

    public ServiceNotFoundException() {}

    public ServiceNotFoundException(String message) {
        super(message);
    }
}
