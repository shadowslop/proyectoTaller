package com.taller.tallerbackend.exceptions;

public class MantenimientoNotFoundException extends RuntimeException {
    public MantenimientoNotFoundException() {
        super("Cita de mantenimiento no encontrada");
    }
}