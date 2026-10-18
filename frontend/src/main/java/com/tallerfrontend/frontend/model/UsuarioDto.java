package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class UsuarioDto {
    private UUID id;
    private String nombre;
    private String password;
    private String rol;
    private SedeDto sedeId; // objeto completo, para leer la respuesta del GET

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public SedeDto getSedeId() { return sedeId; }
    public void setSedeId(SedeDto sedeId) { this.sedeId = sedeId; }
}