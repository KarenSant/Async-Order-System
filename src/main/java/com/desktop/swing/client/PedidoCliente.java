package com.desktop.swing.client;

import com.desktop.swing.model.PedidoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.net.http.*;

public class PedidoCliente {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String BASE_URL = "http://localhost:8080/api/pedidos";

    public String enviarPedido(PedidoRequest pedido) throws Exception {
        String json = mapper.writeValueAsString(pedido);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 202) throw new RuntimeException("Erro: " + response.body());
        return mapper.readTree(response.body()).get("id").asText();
    }

    public String consultarStatus(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/status/" + id))
                .GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}