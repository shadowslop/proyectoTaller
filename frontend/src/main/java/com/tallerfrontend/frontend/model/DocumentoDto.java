package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;

public class DocumentoDto {
    private String vehiculoPlaca;
    private String tipoDocumento;
    private OffsetDateTime fechaVencimiento;
    private String empresa;

    public DocumentoDto(String vehiculoPlaca, String tipoDocumento, OffsetDateTime fechaVencimiento, String empresa) {
        this.vehiculoPlaca = vehiculoPlaca;
        this.tipoDocumento = tipoDocumento;
        this.fechaVencimiento = fechaVencimiento;
        this.empresa=empresa;
    }

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public OffsetDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(OffsetDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

}