package org.br.view;

import org.br.controller.ClienteController;
import org.br.dto.ClienteDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class ClienteFormPanel extends JPanel implements Refreshable {

    private JTextField txtNome, txtEmail, txtEndereco;
    private JFormattedTextField txtCpfCnpj, txtTelefone;
    private JButton btnSalvar, btnCancelar;
    private final ClienteController clienteController;
    private final MainFrame mainFrame;
    private Long currentId = null;

    public ClienteFormPanel(ClienteController clienteController, MainFrame mainFrame) {
        this.clienteController = clienteController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Informações do Cliente");
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

        addLabelAndField(formPanel, "Nome Completo*", txtNome = new JTextField(25), 0, gbc);
        
        try {
            // Máscara genérica para CPF/CNPJ (permite digitar até 14 números)
            txtCpfCnpj = new JFormattedTextField(new MaskFormatter("##################"));
            txtCpfCnpj.setFocusLostBehavior(JFormattedTextField.COMMIT);
        } catch (ParseException e) { txtCpfCnpj = new JFormattedTextField(); }
        addLabelAndField(formPanel, "CPF ou CNPJ*", txtCpfCnpj, 1, gbc);

        try {
            // Máscara para Telefone (11) 99999-9999
            MaskFormatter telMask = new MaskFormatter("(##) #####-####");
            telMask.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(telMask);
        } catch (ParseException e) { txtTelefone = new JFormattedTextField(); }
        addLabelAndField(formPanel, "Telefone Contato", txtTelefone, 2, gbc);

        addLabelAndField(formPanel, "E-mail", txtEmail = new JTextField(25), 3, gbc);
        addLabelAndField(formPanel, "Endereço Residencial", txtEndereco = new JTextField(25), 4, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setOpaque(false);
        btnCancelar = new JButton("Descartar");
        UIUtils.setRoundButton(btnCancelar, Color.GRAY);
        btnSalvar = new JButton("Salvar Alterações");
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
            if (txtNome.getText().trim().isEmpty()) { UIUtils.setErrorState(txtNome, true); hasError = true; }
            else UIUtils.setErrorState(txtNome, false);
            
            String doc = txtCpfCnpj.getText().trim();
            if (doc.isEmpty()) { UIUtils.setErrorState(txtCpfCnpj, true); hasError = true; }
            else UIUtils.setErrorState(txtCpfCnpj, false);

            if (hasError) return;

            ClienteDTO dto = ClienteDTO.builder()
                    .id(currentId).nome(txtNome.getText())
                    .cpfCnpj(txtCpfCnpj.getText().replaceAll("[^0-9]", ""))
                    .telefone(txtTelefone.getText().replaceAll("[^0-9]", ""))
                    .email(txtEmail.getText()).endereco(txtEndereco.getText()).build();
            
            clienteController.salvar(dto);
            mainFrame.showPanel("clienteList");
        });
        btnCancelar.addActionListener(e -> mainFrame.showPanel("clienteList"));
    }

    public void setClienteParaEdicao(ClienteDTO dto) {
        currentId = (dto != null) ? dto.getId() : null;
        txtNome.setText((dto != null) ? dto.getNome() : "");
        txtCpfCnpj.setText((dto != null) ? dto.getCpfCnpj() : "");
        txtTelefone.setText((dto != null) ? dto.getTelefone() : "");
        txtEmail.setText((dto != null) ? dto.getEmail() : "");
        txtEndereco.setText((dto != null) ? dto.getEndereco() : "");
        UIUtils.setErrorState(txtNome, false);
        UIUtils.setErrorState(txtCpfCnpj, false);
    }

    @Override
    public void refresh() {}
}
