package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.DevolucionRequest;
import com.tallerfrontend.frontend.model.EntregaRequest;
import com.tallerfrontend.frontend.model.PrestamoCreateRequest;
import com.tallerfrontend.frontend.model.PrestamoResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PrestamoService {

    private final ApiClient apiClient = new ApiClient();

    public List<PrestamoResponse> listarPorSede(UUID sedeId) throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/prestamos/sede/" + sedeId, new TypeReference<List<PrestamoResponse>>() {});
    }

    public PrestamoResponse crear(PrestamoCreateRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/prestamos", datos, PrestamoResponse.class);
    }

    public PrestamoResponse devolver(UUID prestamoId, DevolucionRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/prestamos/" + prestamoId + "/devolver", datos, PrestamoResponse.class);
    }
    public PrestamoResponse buscarActivoPorPlaca(String placa) throws IOException, InterruptedException, ApiException {
        return apiClient.get("/api/prestamos/vehiculo/" + placa + "/activo", PrestamoResponse.class);
    }
    public PrestamoResponse registrarEntrega(UUID prestamoId, EntregaRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.put("/api/prestamos/" + prestamoId + "/entrega", datos, PrestamoResponse.class);
    }

    public List<PrestamoResponse> listarPorCliente(String cedula) throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/prestamos/cliente/" + cedula, new TypeReference<List<PrestamoResponse>>() {});
    }
    public PrestamoResponse crearDesdeReserva(UUID reservaId, UUID usuarioId)
            throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/prestamos/desde-reserva/" + reservaId + "?usuarioId=" + usuarioId,
                new HashMap<String, Object>(), PrestamoResponse.class);
    }

    public List<PrestamoResponse> listarReservasVencidas()
            throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/prestamos/alertas/reservas-vencidas",
                new TypeReference<List<PrestamoResponse>>() {});
    }
}