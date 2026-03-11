package com.desktop.swing.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelTabelaPedidos extends JPanel {
    private final DefaultTableModel modeloTabela = new DefaultTableModel(new Object[]{"ID do Pedido", "Status"}, 0) {
        @Override public boolean isCellEditable(int linha, int coluna) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelTabelaPedidos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Acompanhamento de Status"));

        // Estilização Azul
        tabela.setRowHeight(30);
        tabela.getTableHeader().setBackground(new Color(25, 118, 210));
        tabela.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer renderizadorCentral = new DefaultTableCellRenderer();
        renderizadorCentral.setHorizontalAlignment(JLabel.CENTER);
        tabela.setDefaultRenderer(Object.class, renderizadorCentral);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    public void adicionarNovoPedido(String identificador, String statusInicial) {
        modeloTabela.insertRow(0, new Object[]{identificador, statusInicial});
    }

    public void atualizarStatusPedido(String identificador, String novoStatus) {
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            if (modeloTabela.getValueAt(i, 0).toString().equals(identificador)) {
                modeloTabela.setValueAt(novoStatus, i, 1);
                break;
            }
        }
    }
}