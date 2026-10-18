package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tipo_vehiculo")
public class TipoVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "preciodia", nullable = false)
    private BigDecimal precioDia;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public TipoVehiculo() {}

    public TipoVehiculo(String nombre, BigDecimal precioDia) {
        this.nombre = nombre;
        this.precioDia = precioDia;
        this.activo = true;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}