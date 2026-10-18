package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;

// Espejo exacto de VehiculoResponse.java del backend (tipo como texto plano),
// usado SOLO dentro de PrestamoResponse y ReservaResponse.
// No confundir con VehiculoDto, que se usa para /api/vehiculos (tipo como objeto TipoVehiculoDto).
public class VehiculoResumenDto {
    private String placa;
    private String tipo;
    private BigDecimal precioDia;
    private Boolean estado;
    private SedeDto sede;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public SedeDto getSede() { return sede; }
    public void setSede(SedeDto sede) { this.sede = sede; }
}