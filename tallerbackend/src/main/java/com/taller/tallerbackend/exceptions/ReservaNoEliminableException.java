package com.taller.tallerbackend.exceptions;

public class ReservaNoEliminableException extends RuntimeException {
    public ReservaNoEliminableException() {
        super("Solo se pueden eliminar reservas canceladas, finalizadas o cuya fecha ya paso");
    }
}