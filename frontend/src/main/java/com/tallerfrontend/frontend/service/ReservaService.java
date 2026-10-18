package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.ReservaActualizarRequest;
import com.tallerfrontend.frontend.model.ReservaCreateRequest;
import com.tallerfrontend.frontend.model.ReservaResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReservaService {

    private final ApiClient apiClient = new ApiClient();

    public ReservaResponse crear(ReservaCreateRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/reservas", datos, ReservaResponse.class);
    }

    public ReservaResponse modificar(UUID id, ReservaActualizarRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/reservas/" + id, datos, ReservaResponse.class);
    }

    public ReservaResponse confirmar(UUID id) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/reservas/" + id + "/confirmar", new HashMap<String, Object>(), ReservaResponse.class);
    }

    public ReservaResponse cancelar(UUID id) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/reservas/" + id + "/cancelar", new HashMap<String, Object>(), ReservaResponse.class);
    }

    public void eliminar(UUID id) throws IOException, InterruptedException, ApiException {
        apiClient.delete("/api/reservas/" + id);
    }

    public List<ReservaResponse> listarPorSede(UUID sedeId) throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/reservas/sede/" + sedeId, new TypeReference<List<ReservaResponse>>() {});
    }
}