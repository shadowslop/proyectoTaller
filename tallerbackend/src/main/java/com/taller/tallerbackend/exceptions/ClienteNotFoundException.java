package com.taller.tallerbackend.exceptions;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException() {
        super("Cliente no encontrado");
    }
}
