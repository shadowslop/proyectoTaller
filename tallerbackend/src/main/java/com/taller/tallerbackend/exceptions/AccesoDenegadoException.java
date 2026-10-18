package com.taller.tallerbackend.exceptions;

public class AccesoDenegadoException extends RuntimeException {
    public AccesoDenegadoException() {
        super("No tienes permisos para realizar esta accion");
    }
}