package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.TipoMantenimiento;

import java.time.LocalDate;

public class MantenimientoRequest {
    private String vehiculoPlaca;
    private TipoMantenimiento tipo;
    private LocalDate fecha;

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }
    public TipoMantenimiento getTipo() { return tipo; }
    public void setTipo(TipoMantenimiento tipo) { this.tipo = tipo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}