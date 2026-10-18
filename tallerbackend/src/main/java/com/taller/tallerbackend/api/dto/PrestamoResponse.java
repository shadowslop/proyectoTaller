package com.taller.tallerbackend.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PrestamoResponse {
    private UUID id;
    private ClienteResponse cliente;
    private VehiculoResponse vehiculo;
    private UsuarioResponse usuario;
    private SedeResponse sedePrestamo;
    private SedeResponse sedeDevolucion;
    private OffsetDateTime fechaPrestamo;
    private OffsetDateTime fechaDevolucion;
    private boolean estado;
    private BigDecimal montoTotal;
    private UUID reservaId;
    private OffsetDateTime fechaDevolucionEsperada;
    private BigDecimal recargoMora;



    public PrestamoResponse(UUID id, ClienteResponse cliente, VehiculoResponse vehiculo, UsuarioResponse usuario,
                            SedeResponse sedePrestamo, SedeResponse sedeDevolucion,
                            OffsetDateTime fechaPrestamo, OffsetDateTime fechaDevolucion,
                            boolean estado, BigDecimal montoTotal,
                            UUID reservaId, OffsetDateTime fechaDevolucionEsperada, BigDecimal recargoMora) {
        this.id = id;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.sedePrestamo = sedePrestamo;
        this.sedeDevolucion = sedeDevolucion;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.reservaId = reservaId;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.recargoMora = recargoMora;
    }

    public UUID getId() { return id; }
    public ClienteResponse getCliente() { return cliente; }
    public VehiculoResponse getVehiculo() { return vehiculo; }
    public UsuarioResponse getUsuario() { return usuario; }
    public SedeResponse getSedePrestamo() { return sedePrestamo; }
    public SedeResponse getSedeDevolucion() { return sedeDevolucion; }
    public OffsetDateTime getFechaPrestamo() { return fechaPrestamo; }
    public OffsetDateTime getFechaDevolucion() { return fechaDevolucion; }
    public boolean isEstado() { return estado; }
    public BigDecimal getMontoTotal() { return montoTotal; }

    public UUID getReservaId() { return reservaId; }
    public OffsetDateTime getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public BigDecimal getRecargoMora() { return recargoMora; }
}