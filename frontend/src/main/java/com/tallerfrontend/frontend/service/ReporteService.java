package com.tallerfrontend.frontend.service;

import com.tallerfrontend.frontend.model.ReporteAgendaResponse;
import com.tallerfrontend.frontend.model.ReporteResponse;
import com.tallerfrontend.frontend.model.ReporteVehiculosResponse;

import java.io.IOException;
import java.time.OffsetDateTime;

public class ReporteService {

    private final ApiClient apiClient = new ApiClient();

    public ReporteResponse generar(OffsetDateTime inicio, OffsetDateTime fin) throws IOException, InterruptedException, ApiException {
        String path = "/api/reportes?inicio=" + inicio + "&fin=" + fin;
        return apiClient.get(path, ReporteResponse.class);
    }

    public ReporteAgendaResponse generarAgenda(OffsetDateTime inicio, OffsetDateTime fin) throws IOException, InterruptedException, ApiException {
        String path = "/api/reportes/agenda?inicio=" + inicio + "&fin=" + fin;
        return apiClient.get(path, ReporteAgendaResponse.class);
    }
    public ReporteVehiculosResponse reporteVehiculos() throws IOException, InterruptedException, ApiException {
        return apiClient.get("/api/reportes/vehiculos", ReporteVehiculosResponse.class);
    }
}