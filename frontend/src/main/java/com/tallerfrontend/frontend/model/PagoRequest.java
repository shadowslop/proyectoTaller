package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class PagoRequest {
    private UUID prestamoId;
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA

    public PagoRequest(UUID prestamoId, String metodoPago) {
        this.prestamoId = prestamoId;
        this.metodoPago = metodoPago;
    }

    public UUID getPrestamoId() { return prestamoId; }
    public String getMetodoPago() { return metodoPago; }
}