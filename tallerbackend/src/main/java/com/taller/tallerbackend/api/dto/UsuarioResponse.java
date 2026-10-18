package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class UsuarioResponse {
    private UUID id;
    private String nombre;
    private String rol;

    public UsuarioResponse(UUID id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
}