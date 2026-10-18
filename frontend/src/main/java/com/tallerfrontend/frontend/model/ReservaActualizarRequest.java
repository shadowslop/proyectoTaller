package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;

public class ReservaActualizarRequest {
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;

    public ReservaActualizarRequest(OffsetDateTime fechaInicio, OffsetDateTime fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
}