package com.tallerfrontend.frontend.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.SedeDto;

import java.io.IOException;
import java.util.List;

public class SedeService {

    private final ApiClient apiClient = new ApiClient();

    public List<SedeDto> listar() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/sedes", new TypeReference<List<SedeDto>>() {});
    }

    public SedeDto crear(SedeDto datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/sedes", datos, SedeDto.class);
    }
}