package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class LoginResponse {
    private String token;
    private UUID usuarioId;
    private String nombre;
    private String rol;
    private UUID sedeId;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
}