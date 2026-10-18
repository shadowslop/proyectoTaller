package com.taller.tallerbackend.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ReservaResponse {
    private UUID id;
    private ClienteResponse cliente;
    private VehiculoResponse vehiculo;
    private SedeResponse sede;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;
    private String estado;

    public ReservaResponse(UUID id, ClienteResponse cliente, VehiculoResponse vehiculo, SedeResponse sede,
                           OffsetDateTime fechaInicio, OffsetDateTime fechaFin, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.sede = sede;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public UUID getId() { return id; }
    public ClienteResponse getCliente() { return cliente; }
    public VehiculoResponse getVehiculo() { return vehiculo; }
    public SedeResponse getSede() { return sede; }
    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
}