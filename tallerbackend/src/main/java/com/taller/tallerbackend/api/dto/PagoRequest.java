package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.MetodoPago;
import java.util.UUID;

public class PagoRequest {
    private UUID prestamoId;
    private MetodoPago metodoPago;

    public UUID getPrestamoId() { return prestamoId; }
    public void setPrestamoId(UUID prestamoId) { this.prestamoId = prestamoId; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
}