package com.taller.tallerbackend.api.dto;

import java.time.OffsetDateTime;

public class ReservaActualizarDto {
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(OffsetDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(OffsetDateTime fechaFin) { this.fechaFin = fechaFin; }
}