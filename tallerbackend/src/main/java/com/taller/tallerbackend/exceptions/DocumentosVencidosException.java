package com.taller.tallerbackend.exceptions;

public class DocumentosVencidosException extends RuntimeException {
    public DocumentosVencidosException() {
        super("El vehiculo tiene documentos vencidos, no se puede prestar");
    }
}
