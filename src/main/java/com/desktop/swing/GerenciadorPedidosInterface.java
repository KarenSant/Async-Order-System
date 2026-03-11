package com.desktop.swing;

import com.desktop.swing.client.PedidoCliente;
import com.desktop.swing.model.PedidoRequest;
import com.desktop.swing.ui.PainelFormularioPedido;
import com.desktop.swing.ui.PainelTabelaPedidos;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class GerenciadorPedidosInterface extends JFrame {
    private final PainelFormularioPedido painelFormulario = new PainelFormularioPedido();
    private final PainelTabelaPedidos painelTabela = new PainelTabelaPedidos();
    private final PedidoCliente clienteApi = new PedidoCliente();
    private final Set<String> pedidosEmProcessamento = new HashSet<>();

    public GerenciadorPedidosInterface() {
        configurarJanela();
        vincularEventos();
        iniciarFluxoDePolling();
    }

    private void configurarJanela() {
        setTitle("Gerenciador de Pedidos - Async Order System");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        add(painelFormulario, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void vincularEventos() {
        painelFormulario.getBotaoEnviar().addActionListener(e -> executarEnvioDePedido());
    }

    private void executarEnvioDePedido() {
        String produto = painelFormulario.getNomeProduto();
        int quantidade = painelFormulario.getQuantidade();

        if (produto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o produto.");
            return;
        }

        new SwingWorker<PedidoRequest, Void>() {
            @Override
            protected PedidoRequest doInBackground() throws Exception {
                PedidoRequest requisicao = new PedidoRequest(UUID.randomUUID(), produto, quantidade, LocalDateTime.now());
                clienteApi.enviarPedido(requisicao);
                return requisicao;
            }

            @Override
            protected void done() {
                try {
                    PedidoRequest pedido = get();
                    String id = pedido.getId().toString();
                    painelTabela.adicionarNovoPedido(id, "🔄 PROCESSANDO...");
                    pedidosEmProcessamento.add(id);
                    painelFormulario.limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GerenciadorPedidosInterface.this, "Erro: " + ex.getCause().getMessage());
                }
            }
        }.execute();
    }

    private void iniciarFluxoDePolling() {
        new javax.swing.Timer(4000, e -> {
            if (pedidosEmProcessamento.isEmpty()) return;
            new ArrayList<>(pedidosEmProcessamento).forEach(this::consultarStatusAssincronamente);
        }).start();
    }

    private void consultarStatusAssincronamente(String identificador) {
        new SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {
                return clienteApi.consultarStatus(identificador);
            }
            @Override protected void done() {
                try {
                    String statusResultante = get();
                    if (statusResultante.equalsIgnoreCase("SUCESSO") || statusResultante.equalsIgnoreCase("FALHA")) {
                        painelTabela.atualizarStatusPedido(identificador, statusResultante);
                        pedidosEmProcessamento.remove(identificador);
                    }
                } catch (Exception ignorada) {}
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciadorPedidosInterface().setVisible(true));
    }
}