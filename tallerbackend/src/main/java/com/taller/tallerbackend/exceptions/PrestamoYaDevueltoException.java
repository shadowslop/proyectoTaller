package com.taller.tallerbackend.exceptions;

public class PrestamoYaDevueltoException extends RuntimeException {
    public PrestamoYaDevueltoException() {
        super("Este prestamo ya fue devuelto");
    }
}
