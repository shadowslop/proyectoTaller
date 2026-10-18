package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name="cliente")
public class Cliente {
    @Id
    @Column(name="cedula", length = 20, nullable = false, unique = true)
    private String cedula;
    @Column(name="nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name="apellido", length = 50, nullable = false)
    private String apellido;
    @Column(name="telefono")
    private String telefono;
    @Column(name="fecha_vencimiento_licencia", nullable = false)
    private LocalDate fechaVencimientoLicencia;
    @Column(name = "numero_licencia", length = 30, nullable = false)
    private String numeroLicencia;

    public Cliente(){}
    public Cliente(String cedula, String nombre, String apellido, String telefono, LocalDate fechaVencimientoLicencia, String numeroLicencia){
        this.cedula=cedula;
        this.nombre=nombre;
        this.apellido=apellido;
        this.telefono=telefono;
        this.fechaVencimientoLicencia=fechaVencimientoLicencia;
        this.numeroLicencia=numeroLicencia;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaVencimientoLicencia() {
        return fechaVencimientoLicencia;
    }

    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) {
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
    }
}