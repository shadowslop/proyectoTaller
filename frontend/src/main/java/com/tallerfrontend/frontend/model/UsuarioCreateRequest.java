package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class UsuarioCreateRequest {
    private String nombre;
    private String password;
    private String rol;
    private UUID sedeId;

    public UsuarioCreateRequest(String nombre, String password, String rol, UUID sedeId) {
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.sedeId = sedeId;
    }

    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public UUID getSedeId() { return sedeId; }
}