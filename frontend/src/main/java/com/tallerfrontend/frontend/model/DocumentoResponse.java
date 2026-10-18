package com.tallerfrontend.frontend.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DocumentoResponse {
    private UUID id;
    private String tipoDocumento;
    private OffsetDateTime fechaVencimiento;
    private VehiculoDto vehiculoId;
    private String empresa;
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public OffsetDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(OffsetDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public VehiculoDto getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(VehiculoDto vehiculoId) { this.vehiculoId = vehiculoId; }

    public String getVehiculoPlacaTexto() { return vehiculoId != null ? vehiculoId.getPlaca() : ""; }
    public String getFechaVencimientoTexto() { return fechaVencimiento != null ? fechaVencimiento.toLocalDate().toString() : ""; }
}