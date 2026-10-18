package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class PrestamoCreateRequest {
    private String clienteCedula;
    private String vehiculoPlaca;
    private UUID usuarioId;
    private UUID sedePrestamoId;

    public PrestamoCreateRequest(String clienteCedula, String vehiculoPlaca, UUID usuarioId, UUID sedePrestamoId) {
        this.clienteCedula = clienteCedula;
        this.vehiculoPlaca = vehiculoPlaca;
        this.usuarioId = usuarioId;
        this.sedePrestamoId = sedePrestamoId;
    }

    public String getClienteCedula() { return clienteCedula; }
    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getSedePrestamoId() { return sedePrestamoId; }
}