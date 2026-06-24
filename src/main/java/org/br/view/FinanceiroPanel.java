package org.br.view;

import org.br.controller.FinanceiroController;
import org.br.dto.LancamentoFinanceiroDTO;
import org.br.model.LancamentoFinanceiro;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FinanceiroPanel extends JPanel implements Refreshable {

    private final FinanceiroController financeiroController;
    private JTable table;
    private DefaultTableModel model;

    public FinanceiroPanel(FinanceiroController financeiroController) {
        this.financeiroController = financeiroController;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Fluxo de Caixa");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnNovaDespesa = new JButton("Lançar Despesa");
        UIUtils.setRoundButton(btnNovaDespesa, UIUtils.COLOR_DANGER);
        headerPanel.add(btnNovaDespesa, BorderLayout.EAST);
        btnNovaDespesa.addActionListener(e -> lancarDespesa());

        JPanel card = UIUtils.createCardPanel();
        
        model = new DefaultTableModel(new String[]{"ID", "Descrição", "Tipo", "Valor", "Vencimento", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        
        table.getColumnModel().getColumn(3).setCellRenderer(UIUtils.getCurrencyRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(UIUtils.getDateRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(Color.WHITE);
        JButton btnBaixar = new JButton("Dar Baixa (Pagar/Receber)");
        UIUtils.setRoundButton(btnBaixar, UIUtils.COLOR_SUCCESS);
        btnBaixar.addActionListener(e -> darBaixa());
        actionsPanel.add(btnBaixar);
        card.add(actionsPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    private void lancarDespesa() {
        JTextField desc = new JTextField();
        JTextField valor = new JTextField();
        JTextField data = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Object[] msg = {"Descrição:", desc, "Valor:", valor, "Data Vencimento (dd/MM/yyyy):", data};
        int option = JOptionPane.showConfirmDialog(this, msg, "Nova Despesa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal val = new BigDecimal(valor.getText().replace(",", "."));
                LocalDate venc = LocalDate.parse(data.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                financeiroController.criarDespesa(desc.getText(), val, venc);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Dados inválidos.");
            }
        }
    }

    private void darBaixa() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) model.getValueAt(row, 0);
            financeiroController.darBaixa(id);
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lançamento.");
        }
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        financeiroController.listarTodos().forEach(lanc -> 
            model.addRow(new Object[]{
                lanc.getId(),
                lanc.getDescricao(),
                lanc.getTipo(),
                lanc.getValor(),
                lanc.getDataVencimento(),
                lanc.getStatus()
            })
        );
    }

    static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setHorizontalAlignment(JLabel.CENTER);
            c.setOpaque(true);
            if (value instanceof String status && !isSelected) {
                switch (status) {
                    case "PAGO" -> { c.setBackground(UIUtils.COLOR_SUCCESS); c.setForeground(Color.WHITE); }
                    case "PENDENTE" -> { c.setBackground(new Color(255, 243, 205)); c.setForeground(new Color(133, 100, 4)); }
                    case "VENCIDO" -> { c.setBackground(UIUtils.COLOR_DANGER); c.setForeground(Color.WHITE); }
                }
            }
            return c;
        }
    }
}
