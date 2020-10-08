package com.kkhill.core.exception;

public class NotSingleStateException extends IllegalThingException {

    public NotSingleStateException() {}

    public NotSingleStateException(String message) {
        super(message);
    }
}
