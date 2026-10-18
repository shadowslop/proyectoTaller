package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.ClienteDto;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClienteService {

    private final ApiClient apiClient = new ApiClient();

    public List<ClienteDto> listar() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/clientes", new TypeReference<List<ClienteDto>>() {});
    }

    public ClienteDto crear(ClienteDto datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/clientes", datos, ClienteDto.class);
    }


    public List<ClienteDto> buscar(String texto) throws IOException, InterruptedException, ApiException {
        String query = URLEncoder.encode(texto == null ? "" : texto, StandardCharsets.UTF_8);
        return apiClient.getList("/api/clientes/buscar?texto=" + query, new TypeReference<List<ClienteDto>>() {});
    }
    public ClienteDto editar(String cedula, ClienteDto datos) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/clientes/" + cedula, datos, ClienteDto.class);
    }

    public void eliminar(String cedula) throws IOException, InterruptedException, ApiException {
        apiClient.delete("/api/clientes/" + cedula);
    }
}