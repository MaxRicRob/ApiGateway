package com.example.ApiGateway.error;

public class ErrorResponseException extends RuntimeException {

    public ErrorResponseException(String message) {
        super(message);
    }
}
