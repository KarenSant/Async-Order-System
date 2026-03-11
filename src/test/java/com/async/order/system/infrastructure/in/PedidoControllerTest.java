package com.async.order.system.infrastructure.in;

import com.async.order.system.service.PedidoStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private PedidoStatusService statusService;

    @Test
    @DisplayName("Deve retornar 202 Accepted quando o pedido for válido (Fluxo Feliz)")
    void criar() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setId(UUID.randomUUID());
        request.setProduto("Notebook Sênior");
        request.setQuantidade(5);
        request.setDataCriacao(LocalDateTime.now());

        String jsonContent = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando a validação falhar")
    void criarComErroValidacao() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setId(UUID.randomUUID());
        request.setProduto("");
        request.setQuantidade(0);
        request.setDataCriacao(LocalDateTime.now());


        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar o status atual do pedido via polling")
    void getStatus() throws Exception {
        UUID id = UUID.randomUUID();
        String statusEsperado = "SUCESSO";
        when(statusService.consultarStatus(id)).thenReturn(statusEsperado);

        mockMvc.perform(get("/api/pedidos/status/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(statusEsperado));
    }
}