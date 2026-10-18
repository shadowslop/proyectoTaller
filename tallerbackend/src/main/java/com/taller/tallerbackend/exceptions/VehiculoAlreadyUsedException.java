package com.taller.tallerbackend.exceptions;

public class VehiculoAlreadyUsedException extends RuntimeException {
    public VehiculoAlreadyUsedException() {
        super("El vehiculo no esta disponible");
    }
}
