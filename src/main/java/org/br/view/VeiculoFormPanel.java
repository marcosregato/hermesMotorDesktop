package org.br.view;

import org.br.controller.ClienteController;
import org.br.controller.VeiculoController;
import org.br.dto.ClienteDTO;
import org.br.dto.VeiculoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.Calendar;
import java.util.regex.Pattern;

public class VeiculoFormPanel extends JPanel implements Refreshable {

    private final VeiculoController veiculoController;
    private final ClienteController clienteController;
    private final MainFrame mainFrame;
    private Long currentId = null;

    private JComboBox<ClienteDTO> cbCliente;
    private JTextField txtPlaca, txtMarca, txtModelo, txtCor;
    private JSpinner spinnerAnoFab, spinnerAnoMod;

    public VeiculoFormPanel(VeiculoController veiculoController, ClienteController clienteController, MainFrame mainFrame) {
        this.veiculoController = veiculoController;
        this.clienteController = clienteController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
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

        // --- Campos do Formulário ---
        addLabelAndField(formPanel, "Cliente*", cbCliente = new JComboBox<>(), 0, gbc);

        txtPlaca = new JTextField(10);
        ((AbstractDocument) txtPlaca.getDocument()).setDocumentFilter(new UpperCaseFilter());
        txtPlaca.setInputVerifier(new PlacaVerifier());
        addLabelAndField(formPanel, "Placa*", txtPlaca, 1, gbc);

        addLabelAndField(formPanel, "Marca*", txtMarca = new JTextField(), 2, gbc);
        addLabelAndField(formPanel, "Modelo*", txtModelo = new JTextField(), 3, gbc);
        addLabelAndField(formPanel, "Cor", txtCor = new JTextField(), 4, gbc);

        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        spinnerAnoFab = new JSpinner(new SpinnerNumberModel(anoAtual, 1950, anoAtual + 1, 1));
        spinnerAnoMod = new JSpinner(new SpinnerNumberModel(anoAtual, 1950, anoAtual + 1, 1));
        addLabelAndField(formPanel, "Ano Fabricação", spinnerAnoFab, 5, gbc);
        addLabelAndField(formPanel, "Ano Modelo", spinnerAnoMod, 6, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        // --- Botões ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setOpaque(false);
        JButton btnCancelar = new JButton("Cancelar");
        UIUtils.setRoundButton(btnCancelar, Color.GRAY);
        JButton btnSalvar = new JButton("Salvar Veículo");
        UIUtils.setRoundButton(btnSalvar, UIUtils.COLOR_PRIMARY);

        btnCancelar.addActionListener(e -> mainFrame.showPanel("veiculoList"));
        btnSalvar.addActionListener(e -> salvar());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private void addLabelAndField(JPanel panel, String label, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        field.setPreferredSize(new Dimension(0, 35));
        panel.add(field, gbc);
    }

    @Override
    public void refresh() {
        cbCliente.removeAllItems();
        clienteController.listarTodos().forEach(cbCliente::addItem);
        setVeiculoParaEdicao(null);
    }

    public void setVeiculoParaEdicao(VeiculoDTO dto) {
        currentId = (dto != null) ? dto.getId() : null;
        txtPlaca.setText(dto != null ? dto.getPlaca() : "");
        txtMarca.setText(dto != null ? dto.getMarca() : "");
        txtModelo.setText(dto != null ? dto.getModelo() : "");
        txtCor.setText(dto != null ? dto.getCor() : "");
        spinnerAnoFab.setValue(dto != null ? dto.getAnoFabricacao() : Calendar.getInstance().get(Calendar.YEAR));
        spinnerAnoMod.setValue(dto != null ? dto.getAnoModelo() : Calendar.getInstance().get(Calendar.YEAR));

        if (dto != null) {
            for (int i = 0; i < cbCliente.getItemCount(); i++) {
                if (cbCliente.getItemAt(i).getId().equals(dto.getIdCliente())) {
                    cbCliente.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void salvar() {
        // ... (lógica de validação e salvamento)
    }

    // --- Classes Internas para Validação da Placa ---
    private static class UpperCaseFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            super.insertString(fb, offset, string.toUpperCase(), attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            super.replace(fb, offset, length, text.toUpperCase(), attrs);
        }
    }

    private static class PlacaVerifier extends InputVerifier {
        private final Pattern placaAntiga = Pattern.compile("[A-Z]{3}[0-9]{4}");
        private final Pattern placaMercosul = Pattern.compile("[A-Z]{3}[0-9][A-Z][0-9]{2}");

        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            String texto = tf.getText().replace("-", "");
            if (texto.isEmpty()) return true;

            if (placaAntiga.matcher(texto).matches() || placaMercosul.matcher(texto).matches()) {
                UIUtils.setErrorState(tf, false);
                return true;
            } else {
                UIUtils.setErrorState(tf, true);
                JOptionPane.showMessageDialog(input, "Formato de placa inválido.\nUse AAA1234 ou AAA1B34.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
}
