package com.tallerfrontend.frontend.service;

public class ApiException extends Exception {
    private final int statusCode;

    public ApiException(int statusCode, String mensaje) {
        super(mensaje);
        this.statusCode = statusCode;
    }

    public ApiException(int statusCode, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.statusCode = statusCode;
    }

    public int getStatusCode() { return statusCode; }
}