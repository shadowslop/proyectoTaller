package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.TipoVehiculoDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TipoVehiculoService {

    private final ApiClient apiClient = new ApiClient();

    public List<TipoVehiculoDto> listarTodos() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/tipos-vehiculo", new TypeReference<List<TipoVehiculoDto>>() {});
    }

    public List<TipoVehiculoDto> listarActivos() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/tipos-vehiculo/activos", new TypeReference<List<TipoVehiculoDto>>() {});
    }

    public TipoVehiculoDto crear(String nombre, BigDecimal precioDia) throws IOException, InterruptedException, ApiException {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("precioDia", precioDia);
        return apiClient.post("/api/tipos-vehiculo", body, TipoVehiculoDto.class);
    }

    public TipoVehiculoDto actualizar(UUID id, String nombre, BigDecimal precioDia) throws IOException, InterruptedException, ApiException {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("precioDia", precioDia);
        return apiClient.put("/api/tipos-vehiculo/" + id, body, TipoVehiculoDto.class);
    }

    public void desactivar(UUID id) throws IOException, InterruptedException, ApiException {
        apiClient.delete("/api/tipos-vehiculo/" + id);
    }
}