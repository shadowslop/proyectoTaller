package com.taller.tallerbackend.exceptions;

public class ReservaNoCancelableException extends RuntimeException {
    public ReservaNoCancelableException() {
        super("Solo se pueden cancelar reservas pendientes o confirmadas antes de su fecha de inicio");
    }
}