package com.taller.tallerbackend.persistencias.entidades;

public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    EN_CURSO,      // <- nuevo: la reserva ya se entregó como préstamo
    FINALIZADA,
    CANCELADA
}