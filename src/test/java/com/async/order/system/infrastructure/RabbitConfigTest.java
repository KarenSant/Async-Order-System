package com.async.order.system.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitConfigTest {

    private RabbitConfig rabbitConfig;

    @BeforeEach
    void setUp() {
        rabbitConfig = new RabbitConfig();
    }

    @Test
    @DisplayName("Deve configurar a fila de entrada com DLQ corretamente")
    void pedidosQueue() {
        Queue queue = rabbitConfig.pedidosQueue();

        assertThat(queue.getName()).isEqualTo(RabbitConfig.QUEUE_ENTRADA);
        assertThat(queue.isDurable()).isTrue();

        // Valida se a DLQ está vinculada nos argumentos da fila principal
        assertThat(queue.getArguments().get("x-dead-letter-exchange")).isEqualTo("");
        assertThat(queue.getArguments().get("x-dead-letter-routing-key")).isEqualTo(RabbitConfig.QUEUE_DLQ);
    }

    @Test
    @DisplayName("Deve configurar a fila de Dead Letter (DLQ)")
    void dlq() {
        Queue queue = rabbitConfig.dlq();
        assertThat(queue.getName()).isEqualTo(RabbitConfig.QUEUE_DLQ);
        assertThat(queue.isDurable()).isTrue();
    }

    @Test
    @DisplayName("Deve configurar a fila de status de sucesso")
    void sucessoQueue() {
        Queue queue = rabbitConfig.sucessoQueue();
        assertThat(queue.getName()).isEqualTo(RabbitConfig.QUEUE_STATUS_SUCESSO);
    }

    @Test
    @DisplayName("Deve configurar a fila de status de falha")
    void falhaQueue() {
        Queue queue = rabbitConfig.falhaQueue();
        assertThat(queue.getName()).isEqualTo(RabbitConfig.QUEUE_STATUS_FALHA);
    }

    @Test
    @DisplayName("Deve configurar o conversor JSON do Jackson")
    void jsonConverter() {
        Jackson2JsonMessageConverter converter = rabbitConfig.jsonConverter();
        assertThat(converter).isNotNull();

        assertThat(converter).isInstanceOf(Jackson2JsonMessageConverter.class);
    }
}