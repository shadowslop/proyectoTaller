package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.MantenimientoDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MantenimientoService {

    private final ApiClient apiClient = new ApiClient();

    public List<MantenimientoDto> listar() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/mantenimientos", new TypeReference<List<MantenimientoDto>>() {});
    }

    public MantenimientoDto agendar(String placa, String tipo, LocalDate fecha)
            throws IOException, InterruptedException, ApiException {
        Map<String, Object> body = new HashMap<>();
        body.put("vehiculoPlaca", placa);
        body.put("tipo", tipo);
        body.put("fecha", fecha.toString());
        return apiClient.post("/api/mantenimientos", body, MantenimientoDto.class);
    }

    public void cancelar(UUID id) throws IOException, InterruptedException, ApiException {
        apiClient.delete("/api/mantenimientos/" + id);
    }
}