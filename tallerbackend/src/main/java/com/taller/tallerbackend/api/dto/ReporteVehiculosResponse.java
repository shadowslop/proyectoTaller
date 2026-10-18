package com.taller.tallerbackend.api.dto;

import java.util.List;

public class ReporteVehiculosResponse {
    private List<VehiculoResponse> disponibles;
    private List<VehiculoResponse> alquilados;
    private List<VehiculoResponse> enMantenimiento;

    public ReporteVehiculosResponse(List<VehiculoResponse> disponibles,
                                    List<VehiculoResponse> alquilados,
                                    List<VehiculoResponse> enMantenimiento) {
        this.disponibles = disponibles;
        this.alquilados = alquilados;
        this.enMantenimiento = enMantenimiento;
    }

    public List<VehiculoResponse> getDisponibles() { return disponibles; }
    public List<VehiculoResponse> getAlquilados() { return alquilados; }
    public List<VehiculoResponse> getEnMantenimiento() { return enMantenimiento; }
}