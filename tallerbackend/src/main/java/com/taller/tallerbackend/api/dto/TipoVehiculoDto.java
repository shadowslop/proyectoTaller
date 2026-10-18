package com.taller.tallerbackend.api.dto;

import java.math.BigDecimal;

public class TipoVehiculoDto {
    private String nombre;
    private BigDecimal precioDia;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
}