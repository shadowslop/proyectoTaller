package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario", nullable = false)
    private String usuario;        // usuario responsable de la modificación

    @Column(name = "accion", nullable = false)
    private String accion;         // CREAR, ACTUALIZAR, ELIMINAR, DEVOLVER...

    @Column(name = "entidad", nullable = false)
    private String entidad;        // Vehiculo, Prestamo, Cliente...

    @Column(name = "detalle", length = 1000)
    private String detalle;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha = OffsetDateTime.now();

    public Auditoria() {}
    public Auditoria(String usuario, String accion, String entidad, String detalle) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.detalle = detalle;
    }
    // getters
    public UUID getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad; }
    public String getDetalle() { return detalle; }
    public OffsetDateTime getFecha() { return fecha; }
}