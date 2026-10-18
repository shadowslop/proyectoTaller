package com.taller.tallerbackend.exceptions;

public class PrestamoNotFoundException extends RuntimeException{
    public PrestamoNotFoundException(){
        super("Prestamo no encontrado");
    }
}
