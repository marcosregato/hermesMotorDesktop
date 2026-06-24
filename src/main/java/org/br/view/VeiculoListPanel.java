package org.br.view;

import org.br.controller.VeiculoController;
import org.br.dto.VeiculoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VeiculoListPanel extends JPanel implements Refreshable {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnNovo, btnEditar, btnExcluir;
    private List<VeiculoDTO> allData;

    private final VeiculoController veiculoController;
    private final MainFrame mainFrame;

    public VeiculoListPanel(VeiculoController veiculoController, MainFrame mainFrame) {
        this.veiculoController = veiculoController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Veículos");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Pesquisar por placa ou marca...");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        txtSearch.setPreferredSize(new Dimension(350, 35));
        
        btnNovo = new JButton("Novo Veículo");
        UIUtils.setRoundButton(btnNovo, UIUtils.COLOR_PRIMARY);

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnNovo, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        JPanel card = UIUtils.createCardPanel();
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Placa", "Marca", "Modelo", "Ano"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.formatTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionsPanel.setBackground(Color.WHITE);
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Remover");
        UIUtils.setRoundButton(btnEditar, Color.GRAY);
        UIUtils.setRoundButton(btnExcluir, UIUtils.COLOR_DANGER);
        actionsPanel.add(btnExcluir);
        actionsPanel.add(btnEditar);
        card.add(actionsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnNovo.addActionListener(e -> {
            mainFrame.getVeiculoFormPanel().setVeiculoParaEdicao(null);
            mainFrame.showPanel("veiculoForm");
        });
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        btnEditar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long id = (Long) table.getValueAt(row, 0);
                VeiculoDTO dto = allData.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
                mainFrame.getVeiculoFormPanel().setVeiculoParaEdicao(dto);
                mainFrame.showPanel("veiculoForm");
            }
        });
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long id = (Long) table.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Remover veículo?", "Confirmação", JOptionPane.YES_NO_OPTION) == 0) {
                    veiculoController.excluir(id);
                    refresh();
                }
            }
        });
    }

    private void filter() {
        if (allData == null) return;
        String text = txtSearch.getText().toLowerCase();
        tableModel.setRowCount(0);
        List<VeiculoDTO> filtered = allData.stream()
                .filter(v -> v.getPlaca().toLowerCase().contains(text) || v.getMarca().toLowerCase().contains(text))
                .collect(Collectors.toList());
        filtered.forEach(v -> tableModel.addRow(new Object[]{v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnoModelo()}));
    }

    @Override
    public void refresh() {
        allData = veiculoController.listarTodos();
        filter();
    }
}
