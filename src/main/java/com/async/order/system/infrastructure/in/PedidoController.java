package com.async.order.system.infrastructure.in;

import com.async.order.system.infrastructure.RabbitConfig;
import com.async.order.system.service.PedidoStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoStatusService statusService;

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoRequest request) {

        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_ENTRADA, request);

        statusService.atualizarStatus(request.getId(), "ENVIADO, AGUARDANDO PROCESSO");

        return ResponseEntity.accepted().body(Map.of("id", request.getId()));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(statusService.consultarStatus(id));
    }
}
