package org.br.view;

import org.br.controller.VeiculoController;
import org.br.dto.OrdensServicoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VeiculoHistoricoPanel extends JPanel implements Refreshable {

    private final VeiculoController veiculoController;
    private final MainFrame mainFrame;
    private JTextField txtPlaca;
    private JTable table;
    private DefaultTableModel model;

    public VeiculoHistoricoPanel(VeiculoController veiculoController, MainFrame mainFrame) {
        this.veiculoController = veiculoController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Histórico Técnico do Veículo");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        txtPlaca = new JTextField();
        txtPlaca.putClientProperty("JTextField.placeholderText", "Digite a placa do veículo...");
        txtPlaca.setPreferredSize(new Dimension(0, 35));
        
        JButton btnBuscar = new JButton("Buscar");
        UIUtils.setRoundButton(btnBuscar, UIUtils.COLOR_PRIMARY);
        btnBuscar.addActionListener(this::buscarHistorico);

        searchPanel.add(txtPlaca, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Tabela
        JPanel card = UIUtils.createCardPanel();
        model = new DefaultTableModel(new String[]{"ID O.S.", "Data Abertura", "Status", "Valor Total", "Relato Cliente"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        table.getColumnModel().getColumn(1).setCellRenderer(UIUtils.getDateRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(UIUtils.getCurrencyRenderer());
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void buscarHistorico(ActionEvent e) {
        String placa = txtPlaca.getText().trim();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite uma placa.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setRowCount(0);
        java.util.List<OrdensServicoDTO> historico = veiculoController.buscarHistorico(placa);
        
        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum histórico encontrado para a placa: " + placa, "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            historico.forEach(os -> model.addRow(new Object[]{
                os.getId(),
                os.getDataAbertura(),
                os.getStatus(),
                os.getValorGeralTotal(),
                os.getRelatoCliente()
            }));
        }
    }

    @Override
    public void refresh() {
        txtPlaca.setText("");
        model.setRowCount(0);
    }
}
