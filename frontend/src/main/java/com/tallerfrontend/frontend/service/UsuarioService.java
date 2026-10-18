package com.tallerfrontend.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tallerfrontend.frontend.model.UsuarioCreateRequest;
import com.tallerfrontend.frontend.model.UsuarioDto;

import java.io.IOException;
import java.util.List;

public class UsuarioService {

    private final ApiClient apiClient = new ApiClient();

    public List<UsuarioDto> listar() throws IOException, InterruptedException, ApiException {
        return apiClient.getList("/api/usuarios", new TypeReference<List<UsuarioDto>>() {});
    }

    public UsuarioDto crear(UsuarioCreateRequest datos) throws IOException, InterruptedException, ApiException {
        return apiClient.post("/api/usuarios", datos, UsuarioDto.class);
    }
}