package com.async.order.system.infrastructure.in;

import com.async.order.system.infrastructure.RabbitConfig;
import com.async.order.system.model.Pedido;
import com.async.order.system.service.PedidoService;
import com.async.order.system.service.PedidoStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoListenerTest {

    @Mock
    private PedidoService pedidoService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private PedidoStatusService statusService;

    @InjectMocks
    private PedidoListener pedidoListener;

    @Test
    @DisplayName("Deve processar pedido com SUCESSO e publicar na fila de status correta")
    void deveProcessarComSucesso() throws Exception {
        UUID id = UUID.randomUUID();
        Map<String, Object> mensagem = Map.of(
                "id", id.toString(),
                "produto", "Teclado Mecânico",
                "quantidade", 2
        );

        pedidoListener.onMessage(mensagem);

        verify(pedidoService, times(1)).processar(any(Pedido.class));
        verify(statusService, times(1)).atualizarStatus(id, "SUCESSO");
        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitConfig.QUEUE_STATUS_SUCESSO), any(Map.class));
    }

    @Test
    @DisplayName("Deve tratar FALHA de processamento, publicar status de falha e lançar AmqpRejectAndDontRequeueException para DLQ")
    void deveTratarFalhaEEnviarParaDLQ() throws Exception {
        UUID id = UUID.randomUUID();
        Map<String, Object> mensagem = Map.of(
                "id", id.toString(),
                "produto", "Mouse Gamer",
                "quantidade", 1
        );

        String erroEsperado = "ExcecaoDeProcessamento";
        doThrow(new RuntimeException(erroEsperado)).when(pedidoService).processar(any(Pedido.class));

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> {
            pedidoListener.onMessage(mensagem);
        });

        verify(statusService, times(1)).atualizarStatus(id, "FALHA");
        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitConfig.QUEUE_STATUS_FALHA), any(Map.class));

        verify(rabbitTemplate, never()).convertAndSend(eq(RabbitConfig.QUEUE_STATUS_SUCESSO), any(Map.class));
    }

}