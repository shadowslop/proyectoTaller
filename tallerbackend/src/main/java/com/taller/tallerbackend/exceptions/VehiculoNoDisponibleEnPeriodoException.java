package com.taller.tallerbackend.exceptions;

public class VehiculoNoDisponibleEnPeriodoException extends RuntimeException {
    public VehiculoNoDisponibleEnPeriodoException() {
        super("El vehiculo ya tiene una reserva en ese periodo de fecha/hora");
    }
}