package com.taller.tallerbackend.exceptions;

public class TipoVehiculoDuplicadoException extends RuntimeException {
    public TipoVehiculoDuplicadoException() {
        super("Ya existe un tipo de vehiculo con ese nombre");
    }
}