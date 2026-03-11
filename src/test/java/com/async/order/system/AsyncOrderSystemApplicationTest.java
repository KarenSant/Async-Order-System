package com.async.order.system;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsyncOrderSystemApplicationTest {

    @MockBean
    private ConnectionFactory connectionFactory;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Deve carregar o contexto da aplicação sem erros")
    void contextLoads() {
        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("Deve garantir que o método main executa com sucesso")
    void main() {
        AsyncOrderSystemApplication.main(new String[] {});
    }

}