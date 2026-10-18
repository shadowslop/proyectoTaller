package com.taller.tallerbackend.exceptions;

public class VehiculoConPrestamoActivoException extends RuntimeException {
    public VehiculoConPrestamoActivoException() {
        super("No se puede eliminar el vehiculo porque tiene un prestamo activo");
    }
}