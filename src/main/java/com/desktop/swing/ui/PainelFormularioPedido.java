package com.desktop.swing.ui;

import javax.swing.*;
import java.awt.*;

public class PainelFormularioPedido extends JPanel {
    private final JTextField campoNomeProduto = new JTextField(20);
    private final JSpinner seletorQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JButton botaoEnviar = new JButton("Enviar Novo Pedido");

    public PainelFormularioPedido() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Registro de Novo Produto"));

        GridBagConstraints configuracao = new GridBagConstraints();
        configuracao.insets = new Insets(10, 10, 10, 10);
        configuracao.anchor = GridBagConstraints.WEST;

        adicionarComponente(new JLabel("Nome do Produto:"), 0, 0, configuracao);
        configuracao.fill = GridBagConstraints.HORIZONTAL;
        adicionarComponente(campoNomeProduto, 1, 0, configuracao);

        configuracao.fill = GridBagConstraints.NONE;
        adicionarComponente(new JLabel("Quantidade Desejada:"), 0, 1, configuracao);
        adicionarComponente(seletorQuantidade, 1, 1, configuracao);

        configuracao.gridwidth = 2;
        configuracao.fill = GridBagConstraints.HORIZONTAL;
        botaoEnviar.setBackground(new Color(25, 118, 210));
        botaoEnviar.setForeground(Color.WHITE);
        botaoEnviar.setFont(new Font("Helvetica", Font.BOLD, 13));
        adicionarComponente(botaoEnviar, 0, 2, configuracao);
    }

    private void adicionarComponente(Component componente, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        add(componente, gbc);
    }

    public String getNomeProduto() {
        return campoNomeProduto.getText().trim();
    }

    public int getQuantidade() {
        return (int) seletorQuantidade.getValue();
    }

    public JButton getBotaoEnviar() {
        return botaoEnviar;
    }
    public void limparCampos() {
        campoNomeProduto.setText(""); seletorQuantidade.setValue(1);
    }
}
