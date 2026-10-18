package com.taller.tallerbackend.persistencias.entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="vehiculo")
public class Vehiculo {
     @Id
     @Column(name="placa", nullable = false, unique = true, length = 6)
     private String placa;
    @ManyToOne
    @JoinColumn(name = "tipovehiculoid")
    private TipoVehiculo tipo;
     @Column(name="preciodia")
     private BigDecimal precioDia;
     @Column(name="estado", nullable = false)
     private Boolean estado;
     @ManyToOne
     @JoinColumn(name="sedeid")
     private Sede sedeId;
     public Vehiculo(){}
    public Vehiculo(String placa, TipoVehiculo tipo, BigDecimal precioDia, Boolean estado, Sede sedeId){
         this.placa=placa;
         this.tipo=tipo;
         this.estado=estado;
         this.sedeId=sedeId;
         this.precioDia=precioDia;
     }


    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Sede getSedeId() {
        return sedeId;
    }

    public void setSedeId(Sede sedeId) {
        this.sedeId = sedeId;
    }
}
