package com.taller.tallerbackend.exceptions;

public class ClientePestramoAlreadyActivedException extends RuntimeException{
    public ClientePestramoAlreadyActivedException(){
        super("El cliente ya tiene un prestamo activo");
    }
}
