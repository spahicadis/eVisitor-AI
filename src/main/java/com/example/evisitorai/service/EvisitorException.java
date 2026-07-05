package com.example.evisitorai.service;

public class EvisitorException extends RuntimeException {

    public EvisitorException(String message) {
        super(message);
    }

    public EvisitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
