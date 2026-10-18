package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="sede")
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;
    @Column(name="nombre")
    private String nombre;
public Sede(){}
    public Sede(UUID id, String nombre){
    this.id=id;
    this.nombre=nombre;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
