package com.taller.tallerbackend.persistencias.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Usuario")
public class Usuario {
 @Id
  @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;
 @Column(name="nombre_usuario")
    private String nombre;
    @Column(name="password")
    @JsonIgnore
    private String password;
 @Enumerated(EnumType.STRING)
 @Column(name="rol")
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede sedeId;
 public Usuario(){}
    public Usuario(UUID id, String nombre, String password, Rol rol, Sede sedeId){
     this.id=id;
     this.nombre=nombre;
     this.password=password;
     this.rol=rol;
     this.sedeId=sedeId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Sede getSedeId() {
        return sedeId;
    }

    public void setSedeId(Sede sedeId) {
        this.sedeId = sedeId;
    }
}