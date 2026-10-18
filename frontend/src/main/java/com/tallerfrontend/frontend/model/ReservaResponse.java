package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
public class ReservaResponse {
    private UUID id;
    private ClienteDto cliente;
    private VehiculoResumenDto vehiculo; // antes: VehiculoDto
    private SedeDto sede;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;
    private String estado;

    public UUID getId() { return id; }
    public ClienteDto getCliente() { return cliente; }
    public VehiculoResumenDto getVehiculo() { return vehiculo; }
    public SedeDto getSede() { return sede; }
    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }

    public String getClienteNombre() {
        return cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "";
    }
    public String getVehiculoPlacaTexto() {
        return vehiculo != null ? vehiculo.getPlaca() : "";
    }
    public String getFechaInicioTexto() {
        return fechaInicio != null ? fechaInicio.toLocalDate() + " " + fechaInicio.toLocalTime().withSecond(0).withNano(0) : "";
    }
    public String getFechaFinTexto() {
        return fechaFin != null ? fechaFin.toLocalDate() + " " + fechaFin.toLocalTime().withSecond(0).withNano(0) : "";
    }
    public boolean puedeConfirmarse() {
        if (!"PENDIENTE".equals(estado) || fechaInicio == null || fechaFin == null) return false;

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = fechaInicio.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime fin = fechaFin.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime habilitadaDesde = inicio.toLocalDate().minusDays(1).atStartOfDay();

        return !ahora.isBefore(habilitadaDesde) && !ahora.isAfter(fin);
    }
    public boolean puedeEntregarse() {
        if (!"CONFIRMADA".equals(estado) || fechaInicio == null) return false;
        return !LocalDate.now().isBefore(fechaInicio.toLocalDate());
    }
    public boolean puedeEliminarse() {
        if (fechaFin == null || "EN_CURSO".equals(estado)) return false;
        boolean terminada = "CANCELADA".equals(estado) || "FINALIZADA".equals(estado);
        return terminada || fechaFin.toLocalDate().isBefore(LocalDate.now());
    }
}