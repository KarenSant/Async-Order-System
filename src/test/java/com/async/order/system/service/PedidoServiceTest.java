package com.async.order.system.service;

import com.async.order.system.model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoServiceTest {

    private final PedidoService pedidoService = new PedidoService();

    @Test
    @DisplayName("Deve respeitar o tempo de processamento entre 1 e 3 segundos")
    void deveRespeitarTempoProcessamento() {
        Pedido pedido = new Pedido(UUID.randomUUID(), "Teste", 1, LocalDateTime.now());
        long inicio = System.currentTimeMillis();

        try {
            pedidoService.processar(pedido);
        } catch (Exception ignored) {
        }

        long fim = System.currentTimeMillis();
        long duracao = fim - inicio;

        assertTrue(duracao >= 1000, "O processamento foi rápido demais: " + duracao + "ms");
        assertTrue(duracao <= 3500, "O processamento demorou demais: " + duracao + "ms");
    }

    @Test
    @DisplayName("Deve lancar ExcecaoDeProcessamento")
    void deveLancarExcecaoAleatoria() {
        Pedido pedido = new Pedido(UUID.randomUUID(), "Teste Erro", 1, LocalDateTime.now());
        boolean erro = false;

        for (int i = 0; i < 50; i++) {
            try {
                pedidoService.processar(pedido);
            } catch (Exception e) {
                if ("ExcecaoDeProcessamento".equals(e.getMessage())) {
                    erro = true;
                    break;
                }
            }
        }

        assertTrue(erro, "A excecao de 20% nao foi disparada em 50 tentativas");
    }

}