package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente clienteId;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculoId;

    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede sedeId;

    @Column(name = "fechainicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fechafin")
    private OffsetDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoReserva estado;


    public Reserva() {}

    public Reserva(Cliente clienteId, Vehiculo vehiculoId, Sede sedeId,
                   OffsetDateTime fechaInicio, OffsetDateTime fechaFin, EstadoReserva estado) {
        this.clienteId = clienteId;
        this.vehiculoId = vehiculoId;
        this.sedeId = sedeId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Cliente getClienteId() { return clienteId; }
    public void setClienteId(Cliente clienteId) { this.clienteId = clienteId; }
    public Vehiculo getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Vehiculo vehiculoId) { this.vehiculoId = vehiculoId; }
    public Sede getSedeId() { return sedeId; }
    public void setSedeId(Sede sedeId) { this.sedeId = sedeId; }
    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(OffsetDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public OffsetDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(OffsetDateTime fechaFin) { this.fechaFin = fechaFin; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

}