package org.br.view;

import org.br.controller.DashboardController;
import org.br.dto.DashboardDTO;
import org.br.dto.PecasCatalogoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel implements Refreshable {

    private final DashboardController dashboardController;
    private JLabel lblMotosPatio, lblOrcamentos, lblFaturamento;
    private JTable tblEstoque;
    private DefaultTableModel modelEstoque;

    public DashboardPanel(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        lblMotosPatio = createCard(cardsPanel, "Motos no Pátio", "0", UIUtils.COLOR_PRIMARY);
        lblOrcamentos = createCard(cardsPanel, "Orçamentos Pendentes", "0", new Color(255, 159, 10));
        lblFaturamento = createCard(cardsPanel, "Faturamento (Entregues)", "R$ 0,00", UIUtils.COLOR_SUCCESS);
        add(cardsPanel, BorderLayout.NORTH);

        JPanel estoquePanel = new JPanel(new BorderLayout(10, 10));
        JLabel lblTituloEstoque = new JLabel("Alertas de Estoque Crítico (< 5 unidades)");
        lblTituloEstoque.setFont(new Font("sans-serif", Font.BOLD, 16));
        lblTituloEstoque.setForeground(UIUtils.COLOR_DANGER);
        
        // Bloqueia a edição das células da tabela
        modelEstoque = new DefaultTableModel(new String[]{"Peça / Produto", "Qtd em Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEstoque = new JTable(modelEstoque);
        UIUtils.formatTable(tblEstoque);
        
        estoquePanel.add(lblTituloEstoque, BorderLayout.NORTH);
        estoquePanel.add(new JScrollPane(tblEstoque), BorderLayout.CENTER);
        add(estoquePanel, BorderLayout.CENTER);
    }

    private JLabel createCard(JPanel parent, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("sans-serif", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);
        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("sans-serif", Font.BOLD, 24));
        lblValue.setForeground(color);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    @Override
    public void refresh() {
        DashboardDTO data = dashboardController.getDadosDashboard();
        lblMotosPatio.setText(String.valueOf(data.getMotosNoPatio()));
        lblOrcamentos.setText(String.valueOf(data.getOrçamentosPendentes()));
        lblFaturamento.setText("R$ " + data.getFaturamentoMes());
        modelEstoque.setRowCount(0);
        for (PecasCatalogoDTO p : data.getPecasEstoqueBaixo()) {
            modelEstoque.addRow(new Object[]{p.getNome(), p.getQuantidadeEstoque()});
        }
    }
}
