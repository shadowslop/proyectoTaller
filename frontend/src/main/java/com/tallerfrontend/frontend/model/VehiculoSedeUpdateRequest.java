package com.tallerfrontend.frontend.model;

import java.util.UUID;

public class VehiculoSedeUpdateRequest {
    private UUID sedeId;

    public VehiculoSedeUpdateRequest(UUID sedeId) {
        this.sedeId = sedeId;
    }

    public UUID getSedeId() { return sedeId; }
    public void setSedeId(UUID sedeId) { this.sedeId = sedeId; }
}