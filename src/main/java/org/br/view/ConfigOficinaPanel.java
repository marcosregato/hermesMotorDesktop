package org.br.view;

import org.br.controller.ConfigOficinaController;
import org.br.model.ConfigOficina;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ConfigOficinaPanel extends JPanel implements Refreshable {

    private final ConfigOficinaController controller;
    private JTextField txtNome, txtCnpj, txtTelefone, txtEndereco, txtLogoPath;
    private JTextArea txtMsgWhatsapp;

    public ConfigOficinaPanel(ConfigOficinaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        initComponents();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome da Oficina:"), gbc);
        txtNome = new JTextField(30); gbc.gridx = 1; form.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("CNPJ:"), gbc);
        txtCnpj = new JTextField(30); gbc.gridx = 1; form.add(txtCnpj, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Telefone:"), gbc);
        txtTelefone = new JTextField(30); gbc.gridx = 1; form.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Endereço:"), gbc);
        txtEndereco = new JTextField(30); gbc.gridx = 1; form.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Mensagem WhatsApp:"), gbc);
        txtMsgWhatsapp = new JTextArea(4, 30);
        gbc.gridx = 1; form.add(new JScrollPane(txtMsgWhatsapp), gbc);

        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Caminho do Logo:"), gbc);
        JPanel logoSelectPanel = new JPanel(new BorderLayout(5, 0));
        txtLogoPath = new JTextField();
        JButton btnSelectLogo = new JButton("Selecionar");
        btnSelectLogo.addActionListener(e -> selecionarLogo());
        logoSelectPanel.add(txtLogoPath, BorderLayout.CENTER);
        logoSelectPanel.add(btnSelectLogo, BorderLayout.EAST);
        gbc.gridx = 1; form.add(logoSelectPanel, gbc);

        JButton btnSalvar = new JButton("Salvar Configurações");
        UIUtils.setRoundButton(btnSalvar, UIUtils.COLOR_PRIMARY);
        btnSalvar.addActionListener(e -> salvar());
        
        add(new JLabel("Minha Oficina - Configurações Gerais", JLabel.CENTER), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void selecionarLogo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtLogoPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    @Override
    public void refresh() {
        ConfigOficina config = controller.getConfig();
        txtNome.setText(config.getNomeOficina());
        txtCnpj.setText(config.getCnpj());
        txtTelefone.setText(config.getTelefone());
        txtEndereco.setText(config.getEndereco());
        txtMsgWhatsapp.setText(config.getMensagemPadraoWhatsapp());
        txtLogoPath.setText(config.getLogoPath());
    }

    private void salvar() {
        ConfigOficina config = ConfigOficina.builder()
                .nomeOficina(txtNome.getText())
                .cnpj(txtCnpj.getText())
                .telefone(txtTelefone.getText())
                .endereco(txtEndereco.getText())
                .mensagemPadraoWhatsapp(txtMsgWhatsapp.getText())
                .logoPath(txtLogoPath.getText())
                .build();
        controller.salvar(config);
        JOptionPane.showMessageDialog(this, "Configurações salvas!");
    }
}
