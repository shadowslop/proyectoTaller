package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class SedeConteoResponse {
    private UUID sedeId;
    private String nombre;
    private long cantidadVehiculos;

    public SedeConteoResponse(UUID sedeId, String nombre, long cantidadVehiculos) {
        this.sedeId = sedeId;
        this.nombre = nombre;
        this.cantidadVehiculos = cantidadVehiculos;
    }

    public UUID getSedeId() { return sedeId; }
    public String getNombre() { return nombre; }
    public long getCantidadVehiculos() { return cantidadVehiculos; }
}