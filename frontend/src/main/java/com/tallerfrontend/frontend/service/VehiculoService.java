    package com.tallerfrontend.frontend.service;

    import com.fasterxml.jackson.core.type.TypeReference;
    import com.tallerfrontend.frontend.model.SedeConteoResponse;
    import com.tallerfrontend.frontend.model.VehiculoCreateRequest;
    import com.tallerfrontend.frontend.model.VehiculoDto;
    import com.tallerfrontend.frontend.model.VehiculoSedeUpdateRequest;

    import java.io.IOException;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;
    import java.util.List;
    import java.util.UUID;

    public class VehiculoService {

        private final ApiClient apiClient = new ApiClient();

        public List<VehiculoDto> listarDisponibles() throws IOException, InterruptedException, ApiException {
            return apiClient.getList("/api/vehiculos/disponibles", new TypeReference<List<VehiculoDto>>() {});
        }

        public List<VehiculoDto> listar() throws IOException, InterruptedException, ApiException {
            return apiClient.getList("/api/vehiculos", new TypeReference<List<VehiculoDto>>() {});
        }

        public VehiculoDto crear(VehiculoCreateRequest datos) throws IOException, InterruptedException, ApiException {
            return apiClient.post("/api/vehiculos", datos, VehiculoDto.class);
        }


        public List<VehiculoDto> buscarDisponiblesPorSede(UUID sedeId, String texto) throws IOException, InterruptedException, ApiException {
            String query = URLEncoder.encode(texto == null ? "" : texto, StandardCharsets.UTF_8);
            return apiClient.getList("/api/vehiculos/sede/" + sedeId + "/disponibles/buscar?texto=" + query,
                    new TypeReference<List<VehiculoDto>>() {});
        }

        public void eliminar(String placa) throws IOException, InterruptedException, ApiException {
            apiClient.delete("/api/vehiculos/" + placa);
        }

        public VehiculoDto cambiarSede(String placa, UUID nuevaSedeId) throws IOException, InterruptedException, ApiException {
            return apiClient.put("/api/vehiculos/" + placa + "/sede", new VehiculoSedeUpdateRequest(nuevaSedeId), VehiculoDto.class);
        }

        public List<SedeConteoResponse> contarPorSede() throws IOException, InterruptedException, ApiException {
            return apiClient.getList("/api/vehiculos/conteo-por-sede", new TypeReference<List<SedeConteoResponse>>() {});
        }
    }