package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DevolucionRequest {
    private UUID sedeDevolucionId;
    private Integer kmFinal;              // RF-35
    private Integer combFinal;            // RF-37
    private String novedades;             // RF-38 / RF-39
    private String estadoFinalVehiculo;   // RF-40: DISPONIBLE, EN_MANTENIMIENTO, FUERA_DE_SERVICIO
    private OffsetDateTime fechaDevolucionEsperada; // RF-46

    public DevolucionRequest(UUID sedeDevolucionId) {
        this.sedeDevolucionId = sedeDevolucionId;
    }

    public UUID getSedeDevolucionId() { return sedeDevolucionId; }
    public Integer getKmFinal() { return kmFinal; }
    public void setKmFinal(Integer kmFinal) { this.kmFinal = kmFinal; }
    public Integer getCombFinal() { return combFinal; }
    public void setCombFinal(Integer combFinal) { this.combFinal = combFinal; }
    public String getNovedades() { return novedades; }
    public void setNovedades(String novedades) { this.novedades = novedades; }
    public String getEstadoFinalVehiculo() { return estadoFinalVehiculo; }
    public void setEstadoFinalVehiculo(String estadoFinalVehiculo) { this.estadoFinalVehiculo = estadoFinalVehiculo; }
    public OffsetDateTime getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(OffsetDateTime fechaDevolucionEsperada) { this.fechaDevolucionEsperada = fechaDevolucionEsperada; }
}