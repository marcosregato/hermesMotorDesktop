package org.br.view;

import org.br.controller.CatalogoController;
import org.br.controller.EstoqueController;
import org.br.dto.EstoqueMovimentacaoDTO;
import org.br.dto.PecasCatalogoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EstoquePanel extends JPanel implements Refreshable {

    private final EstoqueController estoqueController;
    private final CatalogoController catalogoController;
    private JTable table;
    private DefaultTableModel model;

    public EstoquePanel(EstoqueController estoqueController, CatalogoController catalogoController) {
        this.estoqueController = estoqueController;
        this.catalogoController = catalogoController;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Controle de Estoque");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnEntrada = new JButton("Registrar Entrada / Compra");
        UIUtils.setRoundButton(btnEntrada, UIUtils.COLOR_SUCCESS);
        headerPanel.add(btnEntrada, BorderLayout.EAST);
        btnEntrada.addActionListener(e -> registrarEntrada());

        JPanel card = UIUtils.createCardPanel();
        
        model = new DefaultTableModel(new String[]{"Data", "Peça", "Tipo", "Qtd", "Observação"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        table.getColumnModel().getColumn(0).setCellRenderer(UIUtils.getDateRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    private void registrarEntrada() {
        JComboBox<PecasCatalogoDTO> cbPecas = new JComboBox<>();
        catalogoController.listarPecas().forEach(cbPecas::addItem);
        
        JTextField txtQtd = new JTextField();
        JTextField txtObs = new JTextField("NF-e ");

        Object[] message = {
            "Selecione a Peça:", cbPecas,
            "Quantidade:", txtQtd,
            "Observação (Ex: NF-e 12345):", txtObs
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Entrada de Peça", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                PecasCatalogoDTO peca = (PecasCatalogoDTO) cbPecas.getSelectedItem();
                int qtd = Integer.parseInt(txtQtd.getText());
                estoqueController.registrarEntrada(peca.getId(), qtd, txtObs.getText());
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar entrada: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        estoqueController.listarMovimentacoes().forEach(mov -> 
            model.addRow(new Object[]{
                mov.getDataMovimentacao(),
                mov.getNomePeca(),
                mov.getTipo(),
                mov.getQuantidade(),
                mov.getObservacao()
            })
        );
    }
}
