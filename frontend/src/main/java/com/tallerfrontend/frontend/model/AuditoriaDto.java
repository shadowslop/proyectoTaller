package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AuditoriaDto {
    private UUID id;
    private String usuario;
    private String accion;
    private String entidad;
    private String detalle;
    private OffsetDateTime fecha;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public OffsetDateTime getFecha() { return fecha; }
    public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }

    public String getFechaTexto() {
        return fecha == null ? "" : fecha.atZoneSameInstant(java.time.ZoneId.systemDefault())
                .toLocalDateTime().toString().replace("T", " ");
    }
}