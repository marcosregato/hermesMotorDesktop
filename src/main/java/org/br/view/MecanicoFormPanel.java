package org.br.view;

import org.br.controller.MecanicoController;
import org.br.dto.MecanicoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class MecanicoFormPanel extends JPanel implements Refreshable {

    private JTextField txtNome, txtEspecialidade;
    private JFormattedTextField txtCpf, txtTelefone;
    private JCheckBox chkAtivo;
    private JButton btnSalvar, btnCancelar;

    private final MecanicoController mecanicoController;
    private final MainFrame mainFrame;
    private Long currentId = null;

    public MecanicoFormPanel(MecanicoController mecanicoController, MainFrame mainFrame) {
        this.mecanicoController = mecanicoController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Cadastro de Mecânico");
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
            txtCpf = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
            txtCpf.setFocusLostBehavior(JFormattedTextField.COMMIT);
        } catch (ParseException e) { txtCpf = new JFormattedTextField(); }
        addLabelAndField(formPanel, "CPF*", txtCpf, 1, gbc);

        try {
            txtTelefone = new JFormattedTextField(new MaskFormatter("(##) #####-####"));
        } catch (ParseException e) { txtTelefone = new JFormattedTextField(); }
        addLabelAndField(formPanel, "Telefone", txtTelefone, 2, gbc);

        addLabelAndField(formPanel, "Especialidade", txtEspecialidade = new JTextField(25), 3, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        chkAtivo = new JCheckBox("Ativo no Sistema", true);
        chkAtivo.setOpaque(false);
        formPanel.add(chkAtivo, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setOpaque(false);
        btnCancelar = new JButton("Cancelar");
        UIUtils.setRoundButton(btnCancelar, Color.GRAY);
        btnSalvar = new JButton("Salvar Mecânico");
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
            
            String cpfVal = txtCpf.getText().replaceAll("[^0-9]", "");
            if (cpfVal.isEmpty()) { UIUtils.setErrorState(txtCpf, true); hasError = true; }
            else UIUtils.setErrorState(txtCpf, false);

            if (hasError) return;

            MecanicoDTO dto = MecanicoDTO.builder()
                    .id(currentId).nome(txtNome.getText())
                    .cpf(cpfVal)
                    .telefone(txtTelefone.getText().replaceAll("[^0-9]", ""))
                    .especialidade(txtEspecialidade.getText())
                    .ativo(chkAtivo.isSelected()).build();
            mecanicoController.salvar(dto);
            mainFrame.showPanel("mecanicoList");
        });
        btnCancelar.addActionListener(e -> mainFrame.showPanel("mecanicoList"));
    }

    public void setMecanicoParaEdicao(MecanicoDTO dto) {
        currentId = (dto != null) ? dto.getId() : null;
        txtNome.setText((dto != null) ? dto.getNome() : "");
        txtCpf.setText((dto != null) ? dto.getCpf() : "");
        txtTelefone.setText((dto != null) ? dto.getTelefone() : "");
        txtEspecialidade.setText((dto != null) ? dto.getEspecialidade() : "");
        chkAtivo.setSelected((dto == null) || dto.getAtivo());
        UIUtils.setErrorState(txtNome, false);
        UIUtils.setErrorState(txtCpf, false);
    }

    @Override
    public void refresh() {}
}
