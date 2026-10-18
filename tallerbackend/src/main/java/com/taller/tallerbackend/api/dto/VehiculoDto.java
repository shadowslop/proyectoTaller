package com.taller.tallerbackend.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class VehiculoDto {
    private String placa;
    private UUID tipoId;
    private BigDecimal precioDia;
    private UUID sedeId;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public UUID getTipoId() { return tipoId; }
    public void setTipoId(UUID tipoId) { this.tipoId = tipoId; }

    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }

    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
}