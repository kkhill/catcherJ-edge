package com.kkhill.core.thing.exception;

public class DuplicateServiceException extends IllegalThingException {

    public DuplicateServiceException() {}

    public DuplicateServiceException(String message) {
        super(message);
    }
}
