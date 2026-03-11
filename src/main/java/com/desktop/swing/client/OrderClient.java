package com.desktop.swing.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String API_URL = "http://localhost:8080/api/pedidos";

    public String enviarPedido(String produto, int qtd) throws Exception {
        Map<String, Object> payload = Map.of(
                "id", UUID.randomUUID(),
                "produto", produto,
                "quantidade", qtd,
                "dataCriacao", LocalDateTime.now().toString()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 202) {
            throw new RuntimeException("Erro " + response.statusCode() + ": " + response.body());
        }
        return mapper.readTree(response.body()).get("id").asText();
    }

    public String consultarStatus(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/status/" + id))
                .GET().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}

