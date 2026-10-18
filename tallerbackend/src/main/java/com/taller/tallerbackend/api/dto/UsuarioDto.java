package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.Rol;

import java.util.UUID;

public class UsuarioDto {
    private String nombre;
    private String password;
    private Rol rol;
    private UUID sedeId;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
}