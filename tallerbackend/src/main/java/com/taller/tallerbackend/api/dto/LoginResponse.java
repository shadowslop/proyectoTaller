package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class LoginResponse {
    private String token;
    private UUID usuarioId;
    private String nombre;
    private String rol;
    private UUID sedeId;

    public LoginResponse(String token, UUID usuarioId, String nombre, String rol, UUID sedeId) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.rol = rol;
        this.sedeId = sedeId;
    }

    public String getToken() { return token; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public UUID getSedeId() { return sedeId; }
}