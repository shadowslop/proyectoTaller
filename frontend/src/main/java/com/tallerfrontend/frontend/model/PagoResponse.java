package com.tallerfrontend.frontend.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PagoResponse {
    private UUID id;
    private String metodoPago;
    private BigDecimal monto;
    private BigDecimal recargoMora;
    private OffsetDateTime fecha;

    public UUID getId() { return id; }
    public String getMetodoPago() { return metodoPago; }
    public BigDecimal getMonto() { return monto; }
    public BigDecimal getRecargoMora() { return recargoMora; }
    public OffsetDateTime getFecha() { return fecha; }
}