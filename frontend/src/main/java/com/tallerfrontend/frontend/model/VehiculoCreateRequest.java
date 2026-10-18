package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;
import java.util.UUID;

public class VehiculoCreateRequest {
    private String placa;
    private UUID tipoId;
    private BigDecimal precioDia;
    private UUID sedeId;

    public VehiculoCreateRequest(String placa, UUID tipoId, BigDecimal precioDia, UUID sedeId) {
        this.placa = placa;
        this.tipoId = tipoId;
        this.precioDia = precioDia;
        this.sedeId = sedeId;
    }

    public String getPlaca() { return placa; }
    public UUID getTipoId() { return tipoId; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public UUID getSedeId() { return sedeId; }
}