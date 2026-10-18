package com.tallerfrontend.frontend.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tallerfrontend.frontend.util.SesionActual;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public <T> T post(String path, Object body, Class<T> tipoRespuesta) throws IOException, InterruptedException, ApiException {
        String json = mapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return procesarRespuesta(response, tipoRespuesta);
    }

    public <T> T put(String path, Object body, Class<T> tipoRespuesta) throws IOException, InterruptedException, ApiException {
        String json = mapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json));

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return procesarRespuesta(response, tipoRespuesta);
    }

    public <T> T get(String path, Class<T> tipoRespuesta) throws IOException, InterruptedException, ApiException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return procesarRespuesta(response, tipoRespuesta);
    }

    private void agregarToken(HttpRequest.Builder builder) {
        String token = SesionActual.getInstancia().getToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private <T> T procesarRespuesta(HttpResponse<String> response, Class<T> tipoRespuesta) throws ApiException {
        try {
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return mapper.readValue(response.body(), tipoRespuesta);
            } else {
                throw new ApiException(response.statusCode(), response.body());
            }
        } catch (IOException e) {
            throw new ApiException(response.statusCode(), "Error al procesar la respuesta del servidor", e);
        }
    }

    public <T> T getList(String path, TypeReference<T> tipoRespuesta) throws IOException, InterruptedException, ApiException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return procesarRespuestaLista(response, tipoRespuesta);
    }

    private <T> T procesarRespuestaLista(HttpResponse<String> response, TypeReference<T> tipoRespuesta) throws ApiException {
        try {
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return mapper.readValue(response.body(), tipoRespuesta);
            } else {
                throw new ApiException(response.statusCode(), response.body());
            }
        } catch (IOException e) {
            throw new ApiException(response.statusCode(), "Error al procesar la respuesta del servidor", e);
        }
    }
    public void delete(String path) throws IOException, InterruptedException, ApiException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE();

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ApiException(response.statusCode(), response.body());
        }
    }

    public String getTexto(String path) throws IOException, InterruptedException, ApiException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();

        agregarToken(builder);

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }
        throw new ApiException(response.statusCode(), response.body());
    }
}