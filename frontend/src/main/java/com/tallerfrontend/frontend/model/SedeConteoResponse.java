package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class SedeConteoResponse {
    private UUID sedeId;
    private String nombre;
    private long cantidadVehiculos;

    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public long getCantidadVehiculos() { return cantidadVehiculos; }
    public void setCantidadVehiculos(long cantidadVehiculos) { this.cantidadVehiculos = cantidadVehiculos; }

    @Override
    public String toString() { return nombre + ": " + cantidadVehiculos + " vehiculo(s)"; }
}