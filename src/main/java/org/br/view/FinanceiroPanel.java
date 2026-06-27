package org.br.view;

import org.br.controller.FinanceiroController;
import org.br.dto.FinanceiroSaldosDTO;
import org.br.dto.LancamentoFinanceiroDTO;
import org.br.model.LancamentoFinanceiro;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FinanceiroPanel extends JPanel implements Refreshable {

    private final FinanceiroController financeiroController;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblSaldoDia, lblSaldoMes, lblAReceber;
    private JSpinner spinnerInicio, spinnerFim;

    public FinanceiroPanel(FinanceiroController financeiroController) {
        this.financeiroController = financeiroController;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Fluxo de Caixa");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel saldosPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        saldosPanel.setOpaque(false);
        saldosPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        lblSaldoDia = createSaldoCard(saldosPanel, "Saldo do Dia", UIUtils.COLOR_PRIMARY);
        lblSaldoMes = createSaldoCard(saldosPanel, "Saldo do Mês", UIUtils.COLOR_SUCCESS);
        lblAReceber = createSaldoCard(saldosPanel, "Contas a Receber", UIUtils.COLOR_DANGER);
        
        lblSaldoDia.setName("lblSaldoDia");
        lblSaldoMes.setName("lblSaldoMes");
        lblAReceber.setName("lblAReceber");

        headerPanel.add(saldosPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        spinnerInicio = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        spinnerFim = new JSpinner(new SpinnerDateModel());

        spinnerInicio.setEditor(new JSpinner.DateEditor(spinnerInicio, "dd/MM/yyyy"));
        spinnerFim.setEditor(new JSpinner.DateEditor(spinnerFim, "dd/MM/yyyy"));
        spinnerInicio.addChangeListener(e -> refresh());
        spinnerFim.addChangeListener(e -> refresh());
        
        filterPanel.add(new JLabel("Período de:"));
        filterPanel.add(spinnerInicio);
        filterPanel.add(new JLabel("até:"));
        filterPanel.add(spinnerFim);
        card.add(filterPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Descrição", "Tipo", "Valor", "Vencimento", "Status Pgto"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        table.getColumnModel().getColumn(3).setCellRenderer(UIUtils.getCurrencyRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(UIUtils.getDateRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusPagamentoRenderer());
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(Color.WHITE);
        JButton btnNovaDespesa = new JButton("Lançar Despesa");
        UIUtils.setRoundButton(btnNovaDespesa, UIUtils.COLOR_DANGER);
        btnNovaDespesa.addActionListener(e -> lancarDespesa());
        JButton btnBaixar = new JButton("Dar Baixa");
        UIUtils.setRoundButton(btnBaixar, UIUtils.COLOR_SUCCESS);
        btnBaixar.addActionListener(e -> darBaixa());
        actionsPanel.add(btnNovaDespesa);
        actionsPanel.add(btnBaixar);
        card.add(actionsPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private JLabel createSaldoCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("sans-serif", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);
        JLabel lblValue = new JLabel("R$ 0,00", JLabel.CENTER);
        lblValue.setFont(new Font("sans-serif", Font.BOLD, 22));
        lblValue.setForeground(color);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    @Override
    public void refresh() {
        FinanceiroSaldosDTO saldos = financeiroController.getSaldos();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblSaldoDia.setText(currencyFormat.format(saldos.getSaldoDoDia()));
        lblSaldoMes.setText(currencyFormat.format(saldos.getSaldoDoMes()));
        lblAReceber.setText(currencyFormat.format(saldos.getContasAReceber()));

        LocalDate inicio = ((Date) spinnerInicio.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fim = ((Date) spinnerFim.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        model.setRowCount(0);
        financeiroController.listarPorPeriodo(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX)).forEach(lanc -> 
            model.addRow(new Object[]{
                lanc.getId(), lanc.getDescricao(), lanc.getTipo(),
                lanc.getValor(), lanc.getDataVencimento(), lanc.getStatus()
            })
        );
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
                JOptionPane.showMessageDialog(this, "Dados inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Selecione um lançamento.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    static class StatusPagamentoRenderer extends DefaultTableCellRenderer {
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
                    default -> { c.setBackground(Color.WHITE); c.setForeground(Color.BLACK); }
                }
            }
            return c;
        }
    }
}
