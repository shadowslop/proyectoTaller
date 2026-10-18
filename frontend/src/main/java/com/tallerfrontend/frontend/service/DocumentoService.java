package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.DocumentoDto;
import com.tallerfrontend.frontend.model.DocumentoResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DocumentoService {

    private final ApiClient apiClient = new ApiClient();

    public DocumentoResponse crear(DocumentoDto datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/documentos", datos, DocumentoResponse.class);
    }

    public DocumentoResponse actualizar(UUID id, DocumentoDto datos) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/documentos/" + id, datos, DocumentoResponse.class);
    }

    public void eliminar(UUID id) throws IOException, InterruptedException, ApiException {
        apiClient.delete("/api/documentos/" + id);
    }

    public List<DocumentoResponse> listarPorVehiculo(String placa) throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/documentos/vehiculo/" + placa, new TypeReference<List<DocumentoResponse>>() {});
    }

    public List<DocumentoResponse> listarVencidos() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/documentos/vencidos", new TypeReference<List<DocumentoResponse>>() {});
    }
    public List<DocumentoResponse> listarPorVencer() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/documentos/por-vencer", new TypeReference<List<DocumentoResponse>>() {});
    }
}