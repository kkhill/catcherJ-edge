package com.kkhill.core.exception;

public class IllegalServiceCallException extends Exception {
    public IllegalServiceCallException(){}

    public IllegalServiceCallException(String message){
        super(message);
    }
}
