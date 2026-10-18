package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;

public class VehiculoDto {
    private String placa;
    private TipoVehiculoDto tipo;
    private BigDecimal precioDia;
    private Boolean estado;
    private SedeDto sedeId;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public TipoVehiculoDto getTipo() { return tipo; }
    public void setTipo(TipoVehiculoDto tipo) { this.tipo = tipo; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public SedeDto getSedeId() { return sedeId; }
    public void setSedeId(SedeDto sedeId) { this.sedeId = sedeId; }

    public String getTipoNombre() { return tipo != null ? tipo.getNombre() : ""; }

    @Override
    public String toString() { return placa + " - " + getTipoNombre(); }
}