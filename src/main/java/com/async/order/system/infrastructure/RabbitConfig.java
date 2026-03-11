package com.async.order.system.infrastructure;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_ENTRADA = "pedidos.entrada.karen-santana";
    public static final String QUEUE_DLQ = "pedidos.entrada.karen-santana.dlq";
    public static final String QUEUE_STATUS_SUCESSO = "pedidos.status.sucesso.karen-santana";
    public static final String QUEUE_STATUS_FALHA = "pedidos.status.falha.karen-santana";

    @Bean
    public Queue pedidosQueue() {
        return QueueBuilder.durable(QUEUE_ENTRADA)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", QUEUE_DLQ)
                .build();
    }

    @Bean public Queue dlq() {
        return new Queue(QUEUE_DLQ);
    }
    @Bean public Queue sucessoQueue() {
        return new Queue(QUEUE_STATUS_SUCESSO);
    }
    @Bean public Queue falhaQueue() {
        return new Queue(QUEUE_STATUS_FALHA);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
