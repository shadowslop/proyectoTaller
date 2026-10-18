package com.taller.tallerbackend.exceptions;

public class VehiculoNotFoundException extends RuntimeException {
    public VehiculoNotFoundException() {
        super("Vehiculo no encontrado");
    }
}
