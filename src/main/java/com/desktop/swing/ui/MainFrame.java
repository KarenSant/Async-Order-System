package com.desktop.swing.ui;

import com.desktop.swing.client.OrderClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainFrame extends JFrame {
    private DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID Pedido", "Status"}, 0);
    private JTable table = new JTable(tableModel);
    private JTextField txtProduto = new JTextField(15);
    private JSpinner txtQtd = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    private OrderClient api = new OrderClient();
    private Set<String> pedidosEmAberto = new HashSet<>();

    public MainFrame() {
        setTitle("Sistema de Pedidos Assíncronos");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pnlInput = new JPanel(new FlowLayout());
        pnlInput.add(new JLabel("Produto:"));
        pnlInput.add(txtProduto);
        pnlInput.add(new JLabel("Qtd:"));
        pnlInput.add(txtQtd);
        JButton btnEnviar = new JButton("Enviar Pedido");
        pnlInput.add(btnEnviar);

        add(pnlInput, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnEnviar.addActionListener(e -> dispararPedido());

        // Polling: timer a cada 4 segundos
        new javax.swing.Timer(4000, e -> processarPolling()).start();

        setLocationRelativeTo(null);
    }

    private void dispararPedido() {
        String prod = txtProduto.getText();
        int qtd = (int) txtQtd.getValue();

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return api.enviarPedido(prod, qtd);
            }

            @Override
            protected void done() {
                try {
                    String id = get();
                    tableModel.insertRow(0, new Object[]{id, "ENVIADO, AGUARDANDO PROCESSO"});
                    pedidosEmAberto.add(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Falha na Validação/Servidor:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void processarPolling() {
        if (pedidosEmAberto.isEmpty()) return;

        for (String id : new ArrayList<>(pedidosEmAberto)) {
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return api.consultarStatus(id);
                }

                @Override
                protected void done() {
                    try {
                        String status = get();
                        if (status.equals("SUCESSO") || status.equals("FALHA")) {
                            atualizarLinhaTabela(id, status);
                            pedidosEmAberto.remove(id);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }.execute();
        }
    }

    private void atualizarLinhaTabela(String id, String status) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    tableModel.setValueAt(status, i, 1);
                    break;
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

