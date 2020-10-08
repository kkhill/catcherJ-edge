package com.kkhill.core.exception;

public class DuplicateServiceException extends IllegalThingException {

    public DuplicateServiceException() {}

    public DuplicateServiceException(String message) {
        super(message);
    }
}
