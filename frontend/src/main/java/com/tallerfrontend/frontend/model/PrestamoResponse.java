package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PrestamoResponse {
    private UUID id;
    private ClienteDto cliente;
    private VehiculoResumenDto vehiculo; // antes: VehiculoDto
    private Object usuario;
    private SedeDto sedePrestamo;
    private SedeDto sedeDevolucion;
    private OffsetDateTime fechaPrestamo;
    private OffsetDateTime fechaDevolucion;
    private boolean estado;
    private BigDecimal montoTotal;
    private UUID reservaId;
    private OffsetDateTime fechaDevolucionEsperada;
    private BigDecimal recargoMora;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ClienteDto getCliente() { return cliente; }
    public void setCliente(ClienteDto cliente) { this.cliente = cliente; }
    public VehiculoResumenDto getVehiculo() { return vehiculo; }
    public void setVehiculo(VehiculoResumenDto vehiculo) { this.vehiculo = vehiculo; }
    public SedeDto getSedePrestamo() { return sedePrestamo; }
    public void setSedePrestamo(SedeDto sedePrestamo) { this.sedePrestamo = sedePrestamo; }
    public SedeDto getSedeDevolucion() { return sedeDevolucion; }
    public void setSedeDevolucion(SedeDto sedeDevolucion) { this.sedeDevolucion = sedeDevolucion; }
    public OffsetDateTime getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(OffsetDateTime fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public OffsetDateTime getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(OffsetDateTime fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public UUID getReservaId() { return reservaId; }
    public void setReservaId(UUID reservaId) { this.reservaId = reservaId; }
    public OffsetDateTime getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(OffsetDateTime f) { this.fechaDevolucionEsperada = f; }
    public BigDecimal getRecargoMora() { return recargoMora; }
    public void setRecargoMora(BigDecimal recargoMora) { this.recargoMora = recargoMora; }

    public String getEstadoTexto() {
        String base = estado ? "ACTIVO" : "DEVUELTO";
        if (reservaId != null) base += " (RESERVA)";
        if (estado && fechaDevolucionEsperada != null
                && OffsetDateTime.now().isAfter(fechaDevolucionEsperada)) {
            base += " - EN MORA";
        }
        return base;
    }

    public String getClienteNombre() { return cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : ""; }
    public String getVehiculoPlacaTexto() { return vehiculo != null ? vehiculo.getPlaca() : ""; }
}