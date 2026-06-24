package org.br.view;

import org.br.controller.ClienteController;
import org.br.dto.ClienteDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteListPanel extends JPanel implements Refreshable {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnNovo, btnEditar, btnExcluir;
    private List<ClienteDTO> allData;

    private final ClienteController clienteController;
    private final MainFrame mainFrame;

    public ClienteListPanel(ClienteController clienteController, MainFrame mainFrame) {
        this.clienteController = clienteController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        // Top Header
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Clientes");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Pesquisar por nome ou CPF...");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        txtSearch.setPreferredSize(new Dimension(350, 35));
        
        btnNovo = new JButton("Novo Cliente");
        UIUtils.setRoundButton(btnNovo, UIUtils.COLOR_PRIMARY);

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnNovo, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Center Card
        JPanel card = UIUtils.createCardPanel();
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF/CNPJ", "Telefone", "E-mail"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.formatTable(table);
        
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editarSelecionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);
        
        // Actions Footer inside Card
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
            mainFrame.getClienteFormPanel().setClienteParaEdicao(null);
            mainFrame.showPanel("clienteForm");
        });
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long id = (Long) table.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Excluir cliente?", "Aviso", JOptionPane.YES_NO_OPTION) == 0) {
                    clienteController.excluir(id);
                    refresh();
                }
            }
        });
    }

    private void filter() {
        if (allData == null) return;
        String text = txtSearch.getText().toLowerCase();
        tableModel.setRowCount(0);
        List<ClienteDTO> filtered = allData.stream()
                .filter(c -> c.getNome().toLowerCase().contains(text) || c.getCpfCnpj().contains(text))
                .collect(Collectors.toList());
        filtered.forEach(c -> tableModel.addRow(new Object[]{c.getId(), c.getNome(), c.getCpfCnpj(), c.getTelefone(), c.getEmail()}));
    }

    private void editarSelecionado() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) table.getValueAt(row, 0);
            ClienteDTO dto = allData.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
            mainFrame.getClienteFormPanel().setClienteParaEdicao(dto);
            mainFrame.showPanel("clienteForm");
        }
    }

    @Override
    public void refresh() {
        allData = clienteController.listarTodos();
        filter();
    }
}
