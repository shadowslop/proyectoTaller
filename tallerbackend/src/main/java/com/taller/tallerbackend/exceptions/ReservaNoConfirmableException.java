package com.taller.tallerbackend.exceptions;

public class ReservaNoConfirmableException extends RuntimeException {
    public ReservaNoConfirmableException(String mensaje) {
        super(mensaje);
    }
}