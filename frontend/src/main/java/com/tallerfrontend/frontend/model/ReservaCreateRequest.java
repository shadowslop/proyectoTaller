package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ReservaCreateRequest {
    private String clienteCedula;
    private String vehiculoPlaca;
    private UUID sedeId;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;

    public ReservaCreateRequest(String clienteCedula, String vehiculoPlaca, UUID sedeId,
                                OffsetDateTime fechaInicio, OffsetDateTime fechaFin) {
        this.clienteCedula = clienteCedula;
        this.vehiculoPlaca = vehiculoPlaca;
        this.sedeId = sedeId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getClienteCedula() { return clienteCedula; }
    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public UUID getSedeId() { return sedeId; }
    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
}