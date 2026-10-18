package com.tallerfrontend.frontend.model;
import java.util.UUID;

public class SedeDto {
    private UUID id;
    private String nombre;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; } // para que se vea bien en el ComboBox
}