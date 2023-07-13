package com.safetynet.alerts.exceptions;

public class AlreadyPresentException extends RuntimeException {
    public AlreadyPresentException(String s) {
        super(s);
    }
}