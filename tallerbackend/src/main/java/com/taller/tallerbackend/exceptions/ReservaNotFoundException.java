package com.taller.tallerbackend.exceptions;

public class ReservaNotFoundException extends RuntimeException {
    public ReservaNotFoundException() {
        super("Reserva no encontrada");
    }
}