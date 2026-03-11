package com.async.order.system.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Pedido(UUID id, String produto, int quantidade, LocalDateTime dataCriacao) {

}

