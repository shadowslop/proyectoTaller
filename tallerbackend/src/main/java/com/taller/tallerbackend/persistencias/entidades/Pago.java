package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Prestamo prestamo;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodopago", nullable = false)
    private MetodoPago metodoPago;   // RF-43

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;        // RF-42

    @Column(name = "recargomora")
    private BigDecimal recargoMora;  // RF-46

    @Column(name = "fecha")
    private OffsetDateTime fecha = OffsetDateTime.now();

    public Pago() {}
    // getters y setters (id, prestamo, metodoPago, monto, recargoMora, fecha)
    public UUID getId() { return id; }
    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public BigDecimal getRecargoMora() { return recargoMora; }
    public void setRecargoMora(BigDecimal recargoMora) { this.recargoMora = recargoMora; }
    public OffsetDateTime getFecha() { return fecha; }

    public void setFecha(OffsetDateTime fecha) {
        this.fecha = fecha;
    }
}