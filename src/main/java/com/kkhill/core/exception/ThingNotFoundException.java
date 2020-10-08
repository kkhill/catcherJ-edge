package com.kkhill.core.exception;

public class ThingNotFoundException extends Exception {

    public ThingNotFoundException() {}

    public ThingNotFoundException(String message) {
        super(message);
    }
}