package com.async.order.system.infrastructure.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoRequest {
    @NotNull(message = "O ID é obrigatório")
    private UUID id;

    @NotBlank(message = "O produto não pode estar vazio")
    private String produto;

    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private int quantidade;

    @NotNull(message = "A data de criação é obrigatória")
    private LocalDateTime dataCriacao;
}
