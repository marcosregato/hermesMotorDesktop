package org.br.view;

import org.br.controller.MecanicoController;
import org.br.dto.MecanicoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MecanicoListPanel extends JPanel implements Refreshable {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnNovo, btnEditar, btnExcluir;
    private List<MecanicoDTO> allData;

    private final MecanicoController mecanicoController;
    private final MainFrame mainFrame;

    public MecanicoListPanel(MecanicoController mecanicoController, MainFrame mainFrame) {
        this.mecanicoController = mecanicoController;
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
        JLabel lblTitulo = new JLabel("Mecânicos");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Pesquisar por nome ou especialidade...");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        txtSearch.setPreferredSize(new Dimension(350, 35));
        
        btnNovo = new JButton("Novo Mecânico");
        UIUtils.setRoundButton(btnNovo, UIUtils.COLOR_PRIMARY);

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnNovo, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        JPanel card = UIUtils.createCardPanel();
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Especialidade", "Ativo"}, 0) {
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
            mainFrame.getMecanicoFormPanel().setMecanicoParaEdicao(null);
            mainFrame.showPanel("mecanicoForm");
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
                MecanicoDTO dto = allData.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
                mainFrame.getMecanicoFormPanel().setMecanicoParaEdicao(dto);
                mainFrame.showPanel("mecanicoForm");
            }
        });
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long id = (Long) table.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Remover mecânico?", "Aviso", JOptionPane.YES_NO_OPTION) == 0) {
                    mecanicoController.excluir(id);
                    refresh();
                }
            }
        });
    }

    private void filter() {
        if (allData == null) return;
        String text = txtSearch.getText().toLowerCase();
        tableModel.setRowCount(0);
        List<MecanicoDTO> filtered = allData.stream()
                .filter(m -> m.getNome().toLowerCase().contains(text) || 
                            (m.getEspecialidade() != null && m.getEspecialidade().toLowerCase().contains(text)))
                .collect(Collectors.toList());
        filtered.forEach(m -> tableModel.addRow(new Object[]{m.getId(), m.getNome(), m.getCpf(), m.getEspecialidade(), m.getAtivo() ? "Sim" : "Não"}));
    }

    @Override
    public void refresh() {
        allData = mecanicoController.listarTodos();
        filter();
    }
}
