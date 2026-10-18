package com.taller.tallerbackend.exceptions;

public class DocumentosIncompletosException extends RuntimeException {
    public DocumentosIncompletosException() {
        super("El vehiculo no tiene todos los documentos registrados (SOAT, tecnomecanica e impuestos), no se puede prestar");
    }
}