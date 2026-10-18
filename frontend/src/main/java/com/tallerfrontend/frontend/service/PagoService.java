package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.PagoRequest;
import com.tallerfrontend.frontend.model.PagoResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PagoService {

    private final ApiClient apiClient = new ApiClient();

    public PagoResponse registrar(PagoRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/pagos", datos, PagoResponse.class);
    }

    public List<PagoResponse> listarPorPrestamo(UUID prestamoId) throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/pagos/prestamo/" + prestamoId, new TypeReference<List<PagoResponse>>() {});
    }

    // RF-44: comprobante en texto plano
    public String comprobante(UUID prestamoId) throws IOException, InterruptedException, ApiException {
        return apiClient.getTexto("/api/pagos/prestamo/" + prestamoId + "/comprobante");
    }

}