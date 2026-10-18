package com.taller.tallerbackend.exceptions;

public class DocumentoDuplicadoException extends RuntimeException {
    public DocumentoDuplicadoException() {
        super("Este vehiculo ya tiene un documento vigente de ese tipo. Actualiza el existente en vez de crear otro.");
    }
}