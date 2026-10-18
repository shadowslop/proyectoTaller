package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.AuditoriaDto;

import java.io.IOException;
import java.util.List;

public class AuditoriaService {

    private final ApiClient apiClient = new ApiClient();

    public List<AuditoriaDto> listar() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/auditoria", new TypeReference<List<AuditoriaDto>>() {});
    }
}