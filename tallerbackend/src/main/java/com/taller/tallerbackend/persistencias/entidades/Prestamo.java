package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;
    @ManyToOne()
    @JoinColumn(name="usuario_id")
    private Usuario usuarioId;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente clienteId;
    @ManyToOne
    @JoinColumn(name="vehiculoId")
    private Vehiculo vehiculoId;
    @ManyToOne
    @JoinColumn(name="sedeprestamoid")
    private Sede sedePrestamoId;
    @ManyToOne
    @JoinColumn(name="sedeevolucionid")
    private Sede sedeDevolucionId;
    @Column(name="fechaprestamo")
    private OffsetDateTime fechaPrestamo;
    @Column(name="fechaDevolucion")
    private OffsetDateTime fechaDevolucion;
    @Column(name="estado")
    private boolean estado;
    @Column(name="montototal")
    private BigDecimal montoTotal;
    @Column(name = "kminicial")
    private Integer kmInicial;
    @Column(name = "kmfinal")
    private Integer kmFinal;
    @Column(name = "combinicial")
    private Integer combInicial;
    @Column(name = "combfinal")
    private Integer combFinal;
    @Column(name = "diferenciakm")
    private Integer diferenciaKm;
    @Column(name = "estadofinalvehiculo")
    @Enumerated(EnumType.STRING)
    private EstadoVehiculo estadoFinalVehiculo;
    @Column(name = "novedades", length = 1000)
    private String novedades;
    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reservaId;

    @Column(name = "fechadevolucionesperada")
    private OffsetDateTime fechaDevolucionEsperada;

    @Column(name = "recargomora")
    private BigDecimal recargoMora;
    // getters y setters
    public Prestamo(){}
    public Prestamo(UUID id, Cliente clienteId, Vehiculo vehiculoId, Sede sedePrestamoId, Sede sedeDevolucionId, OffsetDateTime fechaPrestamo, OffsetDateTime fechaDevolucion, Boolean estado, BigDecimal montoTotal, Usuario usuarioId){
        this.id=id;
        this.clienteId=clienteId;
        this.estado=estado;
        this.fechaPrestamo=fechaPrestamo;
        this.fechaDevolucion=fechaDevolucion;
        this.sedePrestamoId=sedePrestamoId;
        this.sedeDevolucionId=sedeDevolucionId;
        this.vehiculoId=vehiculoId;
        this.montoTotal=montoTotal;
        this.usuarioId=usuarioId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getClienteId() {
        return clienteId;
    }

    public void setClienteId(Cliente clienteId) {
        this.clienteId = clienteId;
    }

    public Vehiculo getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Vehiculo vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public Sede getSedePrestamoId() {
        return sedePrestamoId;
    }

    public void setSedePrestamoId(Sede sedePrestamoId) {
        this.sedePrestamoId = sedePrestamoId;
    }

    public Sede getSedeDevolucionId() {
        return sedeDevolucionId;
    }

    public void setSedeDevolucionId(Sede sedeDevolucionId) {
        this.sedeDevolucionId = sedeDevolucionId;
    }

    public OffsetDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(OffsetDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public OffsetDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(OffsetDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Integer kmInicial) {
        this.kmInicial = kmInicial;
    }

    public Integer getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Integer kmFinal) {
        this.kmFinal = kmFinal;
    }

    public Integer getCombInicial() {
        return combInicial;
    }

    public void setCombInicial(Integer combInicial) {
        this.combInicial = combInicial;
    }

    public Integer getCombFinal() {
        return combFinal;
    }

    public void setCombFinal(Integer combFinal) {
        this.combFinal = combFinal;
    }

    public Integer getDiferenciaKm() {
        return diferenciaKm;
    }

    public void setDiferenciaKm(Integer diferenciaKm) {
        this.diferenciaKm = diferenciaKm;
    }

    public EstadoVehiculo getEstadoFinalVehiculo() {
        return estadoFinalVehiculo;
    }

    public void setEstadoFinalVehiculo(EstadoVehiculo estadoFinalVehiculo) {
        this.estadoFinalVehiculo = estadoFinalVehiculo;
    }

    public String getNovedades() {
        return novedades;
    }

    public void setNovedades(String novedades) {
        this.novedades = novedades;
    }
    public Reserva getReservaId() { return reservaId; }
    public void setReservaId(Reserva reservaId) { this.reservaId = reservaId; }
    public OffsetDateTime getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(OffsetDateTime f) { this.fechaDevolucionEsperada = f; }
    public BigDecimal getRecargoMora() { return recargoMora; }
    public void setRecargoMora(BigDecimal recargoMora) { this.recargoMora = recargoMora; }
}
