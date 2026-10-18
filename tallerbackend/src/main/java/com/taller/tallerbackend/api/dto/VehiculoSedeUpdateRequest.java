package com.taller.tallerbackend.api.dto;

import java.util.UUID;

public class VehiculoSedeUpdateRequest {
    private UUID sedeId;

    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
}