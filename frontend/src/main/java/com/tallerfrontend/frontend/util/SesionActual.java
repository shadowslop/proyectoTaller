package com.tallerfrontend.frontend.util;

import java.util.UUID;

public class SesionActual {
    private static SesionActual instancia;

    private String token;
    private UUID usuarioId;
    private String nombre;
    private String rol;
    private UUID sedeId;

    private SesionActual() {}

    public static SesionActual getInstancia() {
        if (instancia == null) {
            instancia = new SesionActual();
        }
        return instancia;
    }

    public void iniciarSesion(String token, UUID usuarioId, String nombre, String rol, UUID sedeId) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.rol = rol;
        this.sedeId = sedeId;
    }

    public void cerrarSesion() {
        this.token = null;
        this.usuarioId = null;
        this.nombre = null;
        this.rol = null;
        this.sedeId = null;
    }

    public String getToken() { return token; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public UUID getSedeId() { return sedeId; }
    public boolean estaAutenticado() { return token != null; }
}