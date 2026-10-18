package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="documento")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name="tipodocumento")
    TipoDocumento tipoDocumento;
    @Column(name="fechaVencimiento")
    OffsetDateTime fechaVencimiento;
    @ManyToOne
    @JoinColumn(name="vehiculoid")
    Vehiculo vehiculoId;
    @Column(name = "empresa")
    private String empresa;
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    public Documento(){}
    public Documento(TipoDocumento tipoDocumento, UUID id, OffsetDateTime fechaVencimiento, Vehiculo vehiculoId, String empresa){
        this.tipoDocumento = tipoDocumento;
        this.id = id;
        this.fechaVencimiento = fechaVencimiento;
        this.vehiculoId = vehiculoId;
        this.empresa = empresa;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public OffsetDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(OffsetDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Vehiculo getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Vehiculo vehiculoId) {
        this.vehiculoId = vehiculoId;
    }
}
