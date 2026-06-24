package org.br.view;

import org.br.controller.ClienteController;
import org.br.controller.VeiculoController;
import org.br.dto.ClienteDTO;
import org.br.dto.VeiculoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class VeiculoFormPanel extends JPanel implements Refreshable {

    private JComboBox<ClienteDTO> cbCliente;
    private JTextField txtPlaca, txtMarca, txtModelo, txtAnoFabricacao, txtAnoModelo, txtCor;
    private JButton btnSalvar, btnCancelar;

    private final VeiculoController veiculoController;
    private final ClienteController clienteController;
    private final MainFrame mainFrame;
    private Long currentId = null;

    public VeiculoFormPanel(VeiculoController veiculoController, ClienteController clienteController, MainFrame mainFrame) {
        this.veiculoController = veiculoController;
        this.clienteController = clienteController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Informações do Veículo");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Proprietário*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cbCliente = new JComboBox<>();
        cbCliente.setPreferredSize(new Dimension(350, 35));
        formPanel.add(cbCliente, gbc);

        addLabelAndField(formPanel, "Placa*", txtPlaca = new JTextField(10), 1, gbc);
        addLabelAndField(formPanel, "Marca*", txtMarca = new JTextField(20), 2, gbc);
        addLabelAndField(formPanel, "Modelo", txtModelo = new JTextField(20), 3, gbc);
        addLabelAndField(formPanel, "Ano Fabricação", txtAnoFabricacao = new JTextField(5), 4, gbc);
        addLabelAndField(formPanel, "Ano Modelo", txtAnoModelo = new JTextField(5), 5, gbc);
        addLabelAndField(formPanel, "Cor", txtCor = new JTextField(15), 6, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setOpaque(false);
        btnCancelar = new JButton("Descartar");
        UIUtils.setRoundButton(btnCancelar, Color.GRAY);
        btnSalvar = new JButton("Salvar Veículo");
        UIUtils.setRoundButton(btnSalvar, UIUtils.COLOR_PRIMARY);

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private void addLabelAndField(JPanel panel, String label, JTextField field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.GRAY);
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        field.setPreferredSize(new Dimension(350, 35));
        panel.add(field, gbc);
    }

    private void initEvents() {
        btnSalvar.addActionListener(e -> {
            boolean hasError = false;
            if (cbCliente.getSelectedItem() == null) { UIUtils.setErrorState(cbCliente, true); hasError = true; }
            else UIUtils.setErrorState(cbCliente, false);
            if (txtPlaca.getText().trim().isEmpty()) { UIUtils.setErrorState(txtPlaca, true); hasError = true; }
            else UIUtils.setErrorState(txtPlaca, false);
            if (txtMarca.getText().trim().isEmpty()) { UIUtils.setErrorState(txtMarca, true); hasError = true; }
            else UIUtils.setErrorState(txtMarca, false);

            if (hasError) return;

            ClienteDTO cliente = (ClienteDTO) cbCliente.getSelectedItem();
            VeiculoDTO dto = VeiculoDTO.builder()
                    .id(currentId).idCliente(cliente.getId()).placa(txtPlaca.getText())
                    .marca(txtMarca.getText()).modelo(txtModelo.getText())
                    .anoFabricacao(parse(txtAnoFabricacao.getText()))
                    .anoModelo(parse(txtAnoModelo.getText())).cor(txtCor.getText()).build();
            veiculoController.salvar(dto);
            mainFrame.showPanel("veiculoList");
        });
        btnCancelar.addActionListener(e -> mainFrame.showPanel("veiculoList"));
    }

    private int parse(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    public void setVeiculoParaEdicao(VeiculoDTO dto) {
        currentId = (dto != null) ? dto.getId() : null;
        txtPlaca.setText((dto != null) ? dto.getPlaca() : "");
        txtMarca.setText((dto != null) ? dto.getMarca() : "");
        txtModelo.setText((dto != null) ? dto.getModelo() : "");
        txtAnoFabricacao.setText((dto != null) ? String.valueOf(dto.getAnoFabricacao()) : "");
        txtAnoModelo.setText((dto != null) ? String.valueOf(dto.getAnoModelo()) : "");
        txtCor.setText((dto != null) ? dto.getCor() : "");
        UIUtils.setErrorState(cbCliente, false);
        UIUtils.setErrorState(txtPlaca, false);
        UIUtils.setErrorState(txtMarca, false);
    }

    @Override
    public void refresh() {
        cbCliente.removeAllItems();
        clienteController.listarTodos().forEach(cbCliente::addItem);
    }
}
