package com.async.order.system.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PedidoStatusService {
    private final Map<UUID, String> statusStore = new ConcurrentHashMap<>();

    public void atualizarStatus(UUID id, String status) {
        statusStore.put(id, status);
    }

    public String consultarStatus(UUID id) {
        return statusStore.getOrDefault(id, "PROCESSANDO");
    }
}
