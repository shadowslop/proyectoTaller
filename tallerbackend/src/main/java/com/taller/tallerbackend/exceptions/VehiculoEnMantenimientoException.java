package com.taller.tallerbackend.exceptions;

public class VehiculoEnMantenimientoException extends RuntimeException {
    public VehiculoEnMantenimientoException() {
        super("El vehiculo tiene una cita de mantenimiento programada para hoy, no se puede prestar");
    }
}