package com.example.ApiGateway.api.error;

public class ErrorResponseException extends RuntimeException {

    public ErrorResponseException(String message) {
        super(message);
    }
}
