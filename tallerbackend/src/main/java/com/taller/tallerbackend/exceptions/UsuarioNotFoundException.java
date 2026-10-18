package com.taller.tallerbackend.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException() {
        super("Usuario no encontrado");
    }
}
