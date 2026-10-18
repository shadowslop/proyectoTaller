package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;

// Refleja exactamente VehiculoResponse del backend (tipo llega como texto)
public class VehiculoReporteDto {
    private String placa;
    private String tipo;
    private BigDecimal precioDia;
    private Boolean estado;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}