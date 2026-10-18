package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;
import java.util.UUID;

public class TipoVehiculoDto {
    private UUID id;
    private String nombre;
    private BigDecimal precioDia;
    private Boolean activo;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    @Override
    public String toString() { return nombre; } // asi se ve bien en cualquier ComboBox
}