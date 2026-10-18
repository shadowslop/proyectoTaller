package com.tallerfrontend.frontend.model;

import java.time.LocalDate;
import java.util.UUID;

public class MantenimientoDto {
    private UUID id;
    private String vehiculoPlaca;
    private String vehiculoTipo;
    private String tipo;          // "PREVENTIVO" o "ARREGLO"
    private LocalDate fecha;
    private boolean bloqueaHoy;   // true si el mantenimiento es hoy y bloquea prestamos

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }
    public String getVehiculoTipo() { return vehiculoTipo; }
    public void setVehiculoTipo(String vehiculoTipo) { this.vehiculoTipo = vehiculoTipo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public boolean isBloqueaHoy() { return bloqueaHoy; }
    public void setBloqueaHoy(boolean bloqueaHoy) { this.bloqueaHoy = bloqueaHoy; }
}