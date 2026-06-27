package org.br.view;

import org.br.controller.ConfigOficinaController;
import org.br.model.ConfigOficina;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ConfigOficinaPanel extends JPanel implements Refreshable {

    private final ConfigOficinaController controller;
    private JTextField txtNome, txtCnpj, txtTelefone, txtEndereco, txtLogoPath, txtTokenApi, txtCfop;
    private JTextArea txtMsgWhatsapp;
    private JComboBox<ConfigOficina.AmbienteFiscal> cbAmbiente;

    public ConfigOficinaPanel(ConfigOficinaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Configurações Gerais e Fiscais");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Geral", createGeralPanel());
        tabbedPane.addTab("Fiscal", createFiscalPanel());

        add(tabbedPane, BorderLayout.CENTER);

        JButton btnSalvar = new JButton("Salvar Configurações");
        UIUtils.setRoundButton(btnSalvar, UIUtils.COLOR_PRIMARY);
        btnSalvar.addActionListener(e -> salvar());
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(btnSalvar);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createGeralPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField(panel, "Nome da Oficina:", txtNome = new JTextField(30), 0, gbc);
        addLabelAndField(panel, "CNPJ:", txtCnpj = new JTextField(30), 1, gbc);
        addLabelAndField(panel, "Telefone:", txtTelefone = new JTextField(30), 2, gbc);
        addLabelAndField(panel, "Endereço:", txtEndereco = new JTextField(30), 3, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Mensagem WhatsApp:"), gbc);
        txtMsgWhatsapp = new JTextArea(3, 30);
        gbc.gridx = 1; panel.add(new JScrollPane(txtMsgWhatsapp), gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Caminho do Logo:"), gbc);
        JPanel logoSelectPanel = new JPanel(new BorderLayout(5, 0));
        logoSelectPanel.setOpaque(false);
        txtLogoPath = new JTextField();
        JButton btnSelectLogo = new JButton("...");
        btnSelectLogo.addActionListener(e -> selecionarLogo());
        logoSelectPanel.add(txtLogoPath, BorderLayout.CENTER);
        logoSelectPanel.add(btnSelectLogo, BorderLayout.EAST);
        gbc.gridx = 1; panel.add(logoSelectPanel, gbc);

        return panel;
    }

    private JPanel createFiscalPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField(panel, "Token API Fiscal:", txtTokenApi = new JTextField(30), 0, gbc);
        addLabelAndField(panel, "CFOP Padrão:", txtCfop = new JTextField(10), 1, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Ambiente:"), gbc);
        cbAmbiente = new JComboBox<>(ConfigOficina.AmbienteFiscal.values());
        gbc.gridx = 1; panel.add(cbAmbiente, gbc);

        return panel;
    }

    private void addLabelAndField(JPanel panel, String label, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        field.setPreferredSize(new Dimension(0, 35));
        panel.add(field, gbc);
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
        txtTokenApi.setText(config.getTokenApiFiscal());
        txtCfop.setText(config.getCfopPadrao());
        cbAmbiente.setSelectedItem(config.getAmbienteFiscal());
    }

    private void salvar() {
        ConfigOficina config = ConfigOficina.builder()
                .nomeOficina(txtNome.getText())
                .cnpj(txtCnpj.getText())
                .telefone(txtTelefone.getText())
                .endereco(txtEndereco.getText())
                .mensagemPadraoWhatsapp(txtMsgWhatsapp.getText())
                .logoPath(txtLogoPath.getText())
                .tokenApiFiscal(txtTokenApi.getText())
                .cfopPadrao(txtCfop.getText())
                .ambienteFiscal((ConfigOficina.AmbienteFiscal) cbAmbiente.getSelectedItem())
                .build();
        controller.salvar(config);
        JOptionPane.showMessageDialog(this, "Configurações salvas!");
    }
}
