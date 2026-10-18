package com.taller.tallerbackend.exceptions;

public class DocumentoNotFoundException extends RuntimeException {
    public DocumentoNotFoundException() {
        super("Documento no encontrado");
    }
}