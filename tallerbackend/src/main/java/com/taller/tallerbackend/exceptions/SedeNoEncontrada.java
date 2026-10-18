package com.taller.tallerbackend.exceptions;

public class SedeNoEncontrada extends RuntimeException {
    public SedeNoEncontrada() {
        super("Sede no encontrada");
    }
}
