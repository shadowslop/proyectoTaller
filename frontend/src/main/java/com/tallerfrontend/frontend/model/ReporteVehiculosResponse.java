package com.tallerfrontend.frontend.model;

import java.util.List;

public class ReporteVehiculosResponse {
    private List<VehiculoReporteDto> disponibles;
    private List<VehiculoReporteDto> alquilados;
    private List<VehiculoReporteDto> enMantenimiento;

    public List<VehiculoReporteDto> getDisponibles() { return disponibles; }
    public void setDisponibles(List<VehiculoReporteDto> disponibles) { this.disponibles = disponibles; }

    public List<VehiculoReporteDto> getAlquilados() { return alquilados; }
    public void setAlquilados(List<VehiculoReporteDto> alquilados) { this.alquilados = alquilados; }

    public List<VehiculoReporteDto> getEnMantenimiento() { return enMantenimiento; }
    public void setEnMantenimiento(List<VehiculoReporteDto> enMantenimiento) { this.enMantenimiento = enMantenimiento; }
}