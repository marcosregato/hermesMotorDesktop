package org.br.view;

import org.br.dto.OrdensServicoDTO;
import org.br.controller.OrdemServicoController;
import org.br.model.StatusOS;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OSListPanel extends JPanel implements Refreshable {

    private final OrdemServicoController osController;
    private final MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel model;

    public OSListPanel(OrdemServicoController osController, MainFrame mainFrame) {
        this.osController = osController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Ordens de Serviço");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnNova = new JButton("Nova O.S.");
        UIUtils.setRoundButton(btnNova, UIUtils.COLOR_PRIMARY);
        headerPanel.add(btnNova, BorderLayout.EAST);
        btnNova.addActionListener(e -> mainFrame.showPanel("osForm"));

        // Card das Tabelas
        JPanel card = UIUtils.createCardPanel();
        
        model = new DefaultTableModel(new String[]{"ID", "Veículo", "Status", "Total Geral"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        table.getColumnModel().getColumn(3).setCellRenderer(UIUtils.getCurrencyRenderer());

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(JLabel.CENTER);
                if (value instanceof StatusOS status && !isSelected) {
                    c.setOpaque(true);
                    switch (status) {
                        case ORCAMENTO -> { c.setBackground(new Color(255, 243, 205)); c.setForeground(new Color(133, 100, 4)); }
                        case APROVADO -> { c.setBackground(new Color(212, 237, 218)); c.setForeground(new Color(21, 87, 36)); }
                        case EM_EXECUCAO -> { c.setBackground(new Color(207, 226, 255)); c.setForeground(new Color(8, 66, 152)); }
                        case PRONTO -> { c.setBackground(new Color(230, 230, 230)); c.setForeground(Color.DARK_GRAY); }
                        case ENTREGUE -> { c.setBackground(new Color(245, 245, 247)); c.setForeground(Color.LIGHT_GRAY); }
                    }
                }
                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) verDetalhes();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    private void verDetalhes() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) table.getValueAt(row, 0);
            OrdensServicoDTO dto = osController.listarTodas().stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
            mainFrame.getOsDetalhePanel().setOS(dto);
            mainFrame.showPanel("osDetalhe");
        }
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        osController.listarTodas().forEach(os -> 
            model.addRow(new Object[]{os.getId(), "Placa: " + os.getIdVeiculo(), os.getStatus(), os.getValorGeralTotal()}));
    }
}
