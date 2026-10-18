package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class SedeResponse {
    private UUID id;
    private String nombre;

    public SedeResponse(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
}