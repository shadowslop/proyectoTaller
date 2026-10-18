package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class PrestamoRequest {
    private String clienteCedula;
    private String vehiculoPlaca;
    private UUID usuarioId;
    private UUID sedePrestamoId;

    public String getClienteCedula() { return clienteCedula; }
    public void setClienteCedula(String clienteCedula) { this.clienteCedula = clienteCedula; }

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public UUID getSedePrestamoId() { return sedePrestamoId; }
    public void setSedePrestamoId(UUID sedePrestamoId) { this.sedePrestamoId = sedePrestamoId; }
}