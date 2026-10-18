package com.taller.tallerbackend.exceptions;

public class LicenciaVencidaException extends RuntimeException {
    public LicenciaVencidaException() {
        super("No se puede registrar el cliente: la licencia de conduccion esta vencida");
    }
}