package com.async.order.system.service;

import com.async.order.system.model.Pedido;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PedidoService {
        private final Random random = new Random();

        public void processar(Pedido pedido) throws Exception {
            Thread.sleep(1000 + random.nextInt(2000));

            if (random.nextDouble() < 0.2) {
                throw new RuntimeException("ExcecaoDeProcessamento");
            }
        }
}
