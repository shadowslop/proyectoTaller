package com.taller.tallerbackend.api.dto;

import com.taller.tallerbackend.persistencias.entidades.TipoDocumento;

import java.time.OffsetDateTime;

public class DocumentoDto {
    private String vehiculoPlaca;
    private TipoDocumento tipoDocumento;
    private OffsetDateTime fechaVencimiento;
    private String empresa;

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public OffsetDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(OffsetDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }


}