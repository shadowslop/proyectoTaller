package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.EstadoVehiculo;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DevolucionRequest {
    private UUID sedeDevolucionId;
    private Integer kmFinal;              // RF-35
    private Integer combFinal;            // RF-37
    private String novedades;             // RF-38 / RF-39 (observaciones adjuntas)
    private EstadoVehiculo estadoFinalVehiculo; // RF-40
    private java.time.OffsetDateTime fechaDevolucionEsperada;

    public UUID getSedeDevolucionId() { return sedeDevolucionId; }
    public void setSedeDevolucionId(UUID sedeDevolucionId) { this.sedeDevolucionId = sedeDevolucionId; }
    public Integer getKmFinal() { return kmFinal; }
    public void setKmFinal(Integer kmFinal) { this.kmFinal = kmFinal; }
    public Integer getCombFinal() { return combFinal; }
    public void setCombFinal(Integer combFinal) { this.combFinal = combFinal; }
    public String getNovedades() { return novedades; }
    public void setNovedades(String novedades) { this.novedades = novedades; }
    public EstadoVehiculo getEstadoFinalVehiculo() { return estadoFinalVehiculo; }
    public void setEstadoFinalVehiculo(EstadoVehiculo estadoFinalVehiculo) { this.estadoFinalVehiculo = estadoFinalVehiculo; }

    public OffsetDateTime getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public void setFechaDevolucionEsperada(OffsetDateTime fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }
}