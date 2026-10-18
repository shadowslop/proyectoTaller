package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.Mantenimiento;
import com.taller.tallerbackend.persistencias.entidades.TipoMantenimiento;

import java.time.LocalDate;
import java.util.UUID;

public class MantenimientoResponse {
    private UUID id;
    private String vehiculoPlaca;
    private String vehiculoTipo;
    private TipoMantenimiento tipo;
    private LocalDate fecha;
    private boolean bloqueaHoy;

    public MantenimientoResponse(Mantenimiento m) {
        this.id = m.getId();
        this.vehiculoPlaca = m.getVehiculo().getPlaca();
        this.vehiculoTipo = m.getVehiculo().getTipo().getNombre();
        this.tipo = m.getTipo();
        this.fecha = m.getFecha();
        this.bloqueaHoy = m.getFecha().isEqual(LocalDate.now());
    }

    public UUID getId() { return id; }
    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public String getVehiculoTipo() { return vehiculoTipo; }
    public TipoMantenimiento getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public boolean isBloqueaHoy() { return bloqueaHoy; }
}