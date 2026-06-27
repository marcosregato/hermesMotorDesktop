package org.br.view;

import org.br.controller.MecanicoController;
import org.br.controller.OrdemServicoController;
import org.br.controller.VeiculoController;
import org.br.dto.MecanicoDTO;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.VeiculoDTO;
import org.br.model.StatusOS;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class OSFormPanel extends JPanel implements Refreshable {

    private final OrdemServicoController osController;
    private final VeiculoController veiculoController;
    private final MecanicoController mecanicoController;
    private final MainFrame mainFrame;

    private JComboBox<VeiculoDTO> cbVeiculo;
    private JComboBox<MecanicoDTO> cbMecanico;
    private JTextArea txtRelato;
    private JTextField txtKM;
    private JComboBox<String> cbCombustivel;

    public OSFormPanel(OrdemServicoController osController, VeiculoController veiculoController, 
                       MecanicoController mecanicoController, MainFrame mainFrame) {
        this.osController = osController;
        this.veiculoController = veiculoController;
        this.mecanicoController = mecanicoController;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Nova Ordem de Serviço");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Veículo*:"), gbc);
        cbVeiculo = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cbVeiculo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Mecânico:"), gbc);
        cbMecanico = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cbMecanico, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("KM Entrada*:"), gbc);
        txtKM = new JTextField();
        gbc.gridx = 1; formPanel.add(txtKM, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Nível Combustível:"), gbc);
        cbCombustivel = new JComboBox<>(new String[]{"Reserva", "1/4", "Meio", "3/4", "Cheio"});
        gbc.gridx = 1; formPanel.add(cbCombustivel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Relato do Cliente*:"), gbc);
        txtRelato = new JTextArea(4, 25);
        gbc.gridx = 1; formPanel.add(new JScrollPane(txtRelato), gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setOpaque(false);
        JButton btnCancelar = new JButton("Cancelar");
        UIUtils.setRoundButton(btnCancelar, Color.GRAY);
        JButton btnSalvar = new JButton("Abrir O.S.");
        UIUtils.setRoundButton(btnSalvar, UIUtils.COLOR_SUCCESS);
        
        btnCancelar.addActionListener(e -> mainFrame.showPanel("osList"));
        btnSalvar.addActionListener(e -> salvar());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        cbVeiculo.removeAllItems();
        veiculoController.listarTodos().forEach(cbVeiculo::addItem);
        cbMecanico.removeAllItems();
        mecanicoController.listarTodos().forEach(cbMecanico::addItem);
        
        txtKM.setText("");
        txtRelato.setText("");
        UIUtils.setErrorState(txtKM, false);
        UIUtils.setErrorState(txtRelato, false);
        UIUtils.setErrorState(cbVeiculo, false);
    }

    private void salvar() {
        boolean hasError = false;
        
        VeiculoDTO v = (VeiculoDTO) cbVeiculo.getSelectedItem();
        if (v == null) {
            UIUtils.setErrorState(cbVeiculo, true);
            hasError = true;
        } else {
            UIUtils.setErrorState(cbVeiculo, false);
        }
        
        String kmStr = txtKM.getText().trim();
        if (kmStr.isEmpty()) { 
            UIUtils.setErrorState(txtKM, true); 
            hasError = true; 
        } else { 
            UIUtils.setErrorState(txtKM, false); 
        }

        if (txtRelato.getText().trim().isEmpty()) {
            UIUtils.setErrorState(txtRelato, true);
            hasError = true;
        } else {
            UIUtils.setErrorState(txtRelato, false);
        }

        if (hasError) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios (*).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MecanicoDTO m = (MecanicoDTO) cbMecanico.getSelectedItem();
            
            OrdensServicoDTO dto = OrdensServicoDTO.builder()
                    .idVeiculo(v.getId())
                    .idMecanico(m != null ? m.getId() : null)
                    .relatoCliente(txtRelato.getText())
                    .quilometragemEntrada(Integer.parseInt(kmStr))
                    .nivelCombustivel((String) cbCombustivel.getSelectedItem())
                    .status(StatusOS.ORCAMENTO)
                    .build();
            
            osController.salvar(dto);
            JOptionPane.showMessageDialog(this, "O.S. Aberta com Sucesso!");
            mainFrame.showPanel("osList");
        } catch (NumberFormatException ex) {
            UIUtils.setErrorState(txtKM, true);
            JOptionPane.showMessageDialog(this, "KM deve ser um número válido.");
        }
    }
}
