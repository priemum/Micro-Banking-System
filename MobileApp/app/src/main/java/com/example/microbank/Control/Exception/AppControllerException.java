package com.example.microbank.Control.Exception;

public class AppControllerException extends Exception{
    public AppControllerException(String detailMessage) {
        super(detailMessage);
    }

    public AppControllerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
