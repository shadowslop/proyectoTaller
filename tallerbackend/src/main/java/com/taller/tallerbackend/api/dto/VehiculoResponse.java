package com.taller.tallerbackend.api.dto;

import java.math.BigDecimal;

public class VehiculoResponse {
    private String placa;
    private String tipo;
    private BigDecimal precioDia;
    private Boolean estado;
    private SedeResponse sede;

    public VehiculoResponse(String placa, String tipo, BigDecimal precioDia, Boolean estado, SedeResponse sede) {
        this.placa = placa;
        this.tipo = tipo;
        this.precioDia = precioDia;
        this.estado = estado;
        this.sede = sede;
    }

    public String getPlaca() { return placa; }
    public String getTipo() { return tipo; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public Boolean getEstado() { return estado; }
    public SedeResponse getSede() { return sede; }
}