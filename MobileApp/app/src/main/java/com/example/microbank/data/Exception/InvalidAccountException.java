package com.example.microbank.data.Exception;

public class InvalidAccountException extends Exception {
    public InvalidAccountException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidAccountException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
